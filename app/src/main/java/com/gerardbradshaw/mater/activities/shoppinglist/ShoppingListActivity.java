package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.helpers.AsyncTaskScheduler;
import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Step;
import com.gerardbradshaw.mater.room.entities.Summary;
import com.gerardbradshaw.mater.viewholders.StepViewViewHolder;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;
import com.gerardbradshaw.mater.viewmodels.ItemViewModel;
import com.gerardbradshaw.mater.viewmodels.SummaryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private ProgressBar progressBar;
  private LinearLayout contentView;

  private ItemViewModel itemViewModel;
  private SummaryViewModel summaryViewModel;
  private IngredientViewModel ingredientViewModel;

  private ItemListAdapter itemListAdapter;
  private RecyclerView recyclerView;

  private List<Pair<String, List<Ingredient>>> titleIngredientPairs = new ArrayList<>();
  private List<Pair<RecyclerView, ItemListAdapter>> recyclerAndAdapterPairs = new ArrayList<>();

  private AsyncTaskScheduler taskScheduler;

  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);

    itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);

    MaterApplication materApplication = (MaterApplication) getApplication();
    taskScheduler = materApplication.getTaskScheduler();

    recyclerView = findViewById(R.id.shoppingList_recycler);
    progressBar = findViewById(R.id.shoppingList_progressBar);
    contentView = findViewById(R.id.shoppingList_contentLinearLayout);

    // Set up ToolBar
    Toolbar toolbar = findViewById(R.id.shoppingList_toolbar);
    toolbar.setTitle("My Shopping List");
    setSupportActionBar(toolbar);

    // Set up UI
    progressBar.setVisibility(View.VISIBLE);
    contentView.setVisibility(View.GONE);

    buildShoppingList();

  }

  @Override
  protected void onPause() {
    super.onPause();
    // TODO update the database
  }

  private void buildShoppingList() {

    // Load the data from the database
    Runnable pairsRunnable = new Runnable() {
      @Override
      public void run() {
        new LoadTitleIngredientPairsAsyncTask(summaryViewModel, ingredientViewModel).execute();
      }
    };
    taskScheduler.addNewPriorityTask(pairsRunnable);

    // Set up the RecyclerViews
    Runnable recyclerRunnable = new Runnable() {
      @Override
      public void run() {
        new SetUpRecyclerViewsAsyncTask(ShoppingListActivity.this, progressBar,
            contentView, itemListAdapter).execute();
      }
    };
    taskScheduler.addNewPriorityTask(recyclerRunnable);
  }


  // - - - - - - - - - - - - - - - AsyncTasks - - - - - - - - - - - - - - -

  private class LoadTitleIngredientPairsAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private SummaryViewModel summaryViewModel;
    private IngredientViewModel ingredientViewModel;

    private List<Pair<String, List<Ingredient>>> titleIngredientPairs = new ArrayList<>();


    // Constructor
    LoadTitleIngredientPairsAsyncTask(SummaryViewModel summaryViewModel,
                                      IngredientViewModel ingredientViewModel) {
      this.summaryViewModel = summaryViewModel;
      this.ingredientViewModel = ingredientViewModel;
    }


    // AsyncTask methods
    @Override
    protected Void doInBackground(Void... voids) {

      List<Summary> summaryList = summaryViewModel.getAllSummaries();

      // Get the ingredients for each summary
      for (Summary summary : summaryList) {

        List<Ingredient> ingredientList
            = ingredientViewModel.getAllIngredients(summary.getRecipeId());

        Pair<String, List<Ingredient>> titleIngredientPair
            = new Pair<>(summary.getTitle(), ingredientList);

        titleIngredientPairs.add(titleIngredientPair);
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();

      // Save data to Activity
      ShoppingListActivity.this.titleIngredientPairs = titleIngredientPairs;
    }
  }

  private class SetUpRecyclerViewsAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private Context context;
    private ProgressBar progressBar;
    private LinearLayout contentView;
    private ItemListAdapter itemListAdapter;


    // Constructor
    SetUpRecyclerViewsAsyncTask(Context context,
                                ProgressBar progressBar,
                                LinearLayout contentView,
                                ItemListAdapter itemListAdapter) {
      this.progressBar = progressBar;
      this.contentView = contentView;
      this.itemListAdapter = itemListAdapter;
    }


    // AsyncTask methods
    @Override
    protected Void doInBackground(Void... voids) {

      // Clear the ViewHolder references
      recyclerAndAdapterPairs.clear();

      // Instantiate a layout inflater
      LayoutInflater inflater = (LayoutInflater) getApplicationContext()
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      // Get the insert point and clear it
      ViewGroup insertPoint = findViewById(R.id.shoppingList_contentLinearLayout);
      insertPoint.removeAllViews();

      for (Pair pair : titleIngredientPairs) {
        String title = (String) pair.first;
        List<Ingredient> ingredientList = (ArrayList<Ingredient>) pair.second;

        // Create a section title and set the text
        TextView titleView = (TextView) inflater
            .inflate(R.layout.shopping_list_category, insertPoint, false);
        titleView.setText(title);

        // Add a RecyclerView
        RecyclerView recyclerView = (RecyclerView) inflater
            .inflate(R.layout.shopping_list_recycler, insertPoint, false);

        // Create an adapter
        ItemListAdapter adapter = new ItemListAdapter(context);
        adapter.setData(ingredientList);

        // Get the index for insertion and create layout parameters
        int index = 2 * titleIngredientPairs.size() - 2;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Insert the views into the main view
        insertPoint.addView(titleView, index, layoutParams);
        insertPoint.addView(recyclerView, index + 1, layoutParams);
      }
        
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();

      progressBar.setVisibility(View.GONE);
      contentView.setVisibility(View.VISIBLE);
    }
  }

}
