package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.helpers.AsyncTaskScheduler;
import com.gerardbradshaw.mater.helpers.Categories;
import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Summary;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;
import com.gerardbradshaw.mater.viewmodels.SummaryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private ProgressBar progressBar;
  private NestedScrollView contentView;

  private SummaryViewModel summaryViewModel;
  private IngredientViewModel ingredientViewModel;

  private List<Pair<String, List<Ingredient>>> titleIngredientPairs = new ArrayList<>();
  private List<Pair<String, List<Ingredient>>> categoryIngredientPairs = new ArrayList<>();
  private List<Pair<String, List<Ingredient>>> currentPairs = new ArrayList<>();
  private List<Pair<RecyclerView, IngredientListAdapter>> recyclerAndAdapterPairs = new ArrayList<>();

  private AsyncTaskScheduler taskScheduler;

  private boolean categoryView = true;


  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);

    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);

    MaterApplication materApplication = (MaterApplication) getApplication();
    taskScheduler = materApplication.getTaskScheduler();

    progressBar = findViewById(R.id.shoppingList_progressBar);
    contentView = findViewById(R.id.shoppingList_contentView);

    // Set up ToolBar
    Toolbar toolbar = findViewById(R.id.shoppingList_toolbar);
    toolbar.setTitle("My Shopping List");
    setSupportActionBar(toolbar);

    initialSetUp();
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Save the stock level of the ingredients to the database
    for (int i = 0; i < currentPairs.size(); i++) {
      ingredientViewModel.updateIngredients(currentPairs.get(i).second);
    }
  }


  // - - - - - - - - - - - - - - - Options Menu methods - - - - - - - - - - - - - - -

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.shopping_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_sort) {

      categoryView = !categoryView;

      // Save the stock levels that the user may have changed
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          new SaveStockAsyncTask().execute();
        }
      };
      taskScheduler.addNewPriorityTask(runnable);


    }

    return super.onOptionsItemSelected(item);
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void initialSetUp() {
    // Load the data from the database
    Runnable pairsRunnable = new Runnable() {
      @Override
      public void run() {
        new InitialSetUpAsyncTask(summaryViewModel, ingredientViewModel).execute();
      }
    };
    taskScheduler.addNewPriorityTask(pairsRunnable);
  }

  private void buildRecyclerViews() {
    // Clear the ViewHolder references
    recyclerAndAdapterPairs.clear();

    // Instantiate a layout inflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point and clear it
    ViewGroup insertPoint = findViewById(R.id.shoppingList_contentInsertPoint);
    insertPoint.removeAllViews();

    if (categoryView) {
      currentPairs = categoryIngredientPairs;
    }
    else {
      currentPairs = titleIngredientPairs;
    }

    for (Pair pair : currentPairs) {
      String header = (String) pair.first;
      List<Ingredient> ingredientList = (ArrayList<Ingredient>) pair.second;

      // Create a section title and set the text
      TextView titleView = (TextView) inflater
          .inflate(R.layout.shopping_list_category, insertPoint, false);
      titleView.setText(header);

      // Add a NestedScrollView containing a RecyclerView
      NestedScrollView scrollView = (NestedScrollView) inflater
          .inflate(R.layout.shopping_list_recycler, insertPoint, false);

      RecyclerView recyclerView = (RecyclerView) scrollView.getChildAt(0);

      // Create an adapter
      IngredientListAdapter adapter = new IngredientListAdapter(ShoppingListActivity.this);
      adapter.setData(ingredientList);
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListActivity.this));

      // Add the RecyclerView and Adapter to the holder
      recyclerAndAdapterPairs.add(new Pair<>(recyclerView, adapter));

      // Get the index for insertion and create layout parameters
      int index = 2 * recyclerAndAdapterPairs.size() - 2;
      ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

      // Insert the views into the main view
      insertPoint.addView(titleView, index, layoutParams);
      insertPoint.addView(scrollView, index + 1, layoutParams);
    }
  }

  void setUiLoadingFinished() {
    contentView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.INVISIBLE);
  }

  void setUiLoadingStarted() {
    contentView.setVisibility(View.INVISIBLE);
    progressBar.setVisibility(View.VISIBLE);
  }


  // - - - - - - - - - - - - - - - AsyncTasks - - - - - - - - - - - - - - -

  private class InitialSetUpAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private SummaryViewModel summaryViewModel;
    private IngredientViewModel ingredientViewModel;

    private List<Pair<String, List<Ingredient>>> titleIngredientPairs = new ArrayList<>();
    private List<Pair<String, List<Ingredient>>> categoryIngredientPairs = new ArrayList<>();


    // Constructor
    InitialSetUpAsyncTask(SummaryViewModel summaryViewModel,
                          IngredientViewModel ingredientViewModel) {
      this.summaryViewModel = summaryViewModel;
      this.ingredientViewModel = ingredientViewModel;
    }


    // AsyncTask methods
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      setUiLoadingStarted();
    }

    @Override
    protected Void doInBackground(Void... voids) {

      List<Summary> summaryList = summaryViewModel.getAllSummaries();

      // Get the ingredients for each summary and create titleIngredientPairs
      for (Summary summary : summaryList) {

        List<Ingredient> ingredientList
            = ingredientViewModel.getIngredients(summary.getRecipeId());

        Pair<String, List<Ingredient>> titleIngredientPair
            = new Pair<>(summary.getTitle(), ingredientList);

        titleIngredientPairs.add(titleIngredientPair);
      }

      // Get the category for each Ingredient and create categoryIngredientPairs
      List<Ingredient> allIngredientsList = ingredientViewModel.getAllIngredients();
      Map<String, List<Ingredient>> categoryIngredientMap = new HashMap<>();

      for (Ingredient ingredient : allIngredientsList) {
        String categoryTitle = Categories.getUiStringFromName(ingredient.getCategory());

        if (categoryIngredientMap.containsKey(categoryTitle)) {
          categoryIngredientMap.get(categoryTitle).add(ingredient);
        }
        else {
          List<Ingredient> ingredientList = new ArrayList<>();
          ingredientList.add(ingredient);
          categoryIngredientMap.put(categoryTitle, ingredientList);
        }
      }

      for (Map.Entry<String, List<Ingredient>> entry : categoryIngredientMap.entrySet()) {
        String category = entry.getKey();
        List<Ingredient> ingredientList = entry.getValue();
        categoryIngredientPairs.add(new Pair<>(category, ingredientList));
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();

      // Save data to Activity
      ShoppingListActivity.this.titleIngredientPairs = titleIngredientPairs;
      ShoppingListActivity.this.categoryIngredientPairs = categoryIngredientPairs;

      // Build layout
      buildRecyclerViews();
      setUiLoadingFinished();
    }
  }

  private class SaveStockAsyncTask extends AsyncTask<Void, Void, Void> {

    // Constructor
    SaveStockAsyncTask() {
    }

    // AsyncTask methods
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      setUiLoadingStarted();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      for (int i = 0; i < currentPairs.size(); i++) {
        ingredientViewModel.updateIngredientOnCurrentThread(currentPairs.get(i).second);
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();
      initialSetUp();
    }
  }

}
