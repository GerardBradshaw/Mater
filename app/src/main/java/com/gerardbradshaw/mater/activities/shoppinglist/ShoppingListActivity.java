package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.helpers.AsyncTaskScheduler;
import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Summary;
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
        new SetUpRecyclerViewsAsyncTask(progressBar, contentView, itemListAdapter).execute();
      }
    };
    taskScheduler.addNewPriorityTask(recyclerRunnable);
  }


  // - - - - - - - - - - - - - - - AsyncTasks - - - - - - - - - - - - - - -

  private class SetUpRecyclerViewsAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private ProgressBar progressBar;
    private LinearLayout contentView;
    private ItemListAdapter itemListAdapter;


    // Constructor
    SetUpRecyclerViewsAsyncTask(ProgressBar progressBar,
                                LinearLayout contentView,
                                ItemListAdapter itemListAdapter) {
      this.progressBar = progressBar;
      this.contentView = contentView;
      this.itemListAdapter = itemListAdapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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


    // AsyncTask Methods
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

}
