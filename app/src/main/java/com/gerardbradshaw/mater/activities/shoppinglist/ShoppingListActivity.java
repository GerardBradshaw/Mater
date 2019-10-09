package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.gerardbradshaw.mater.room.entities.Item;
import com.gerardbradshaw.mater.room.entities.Step;
import com.gerardbradshaw.mater.room.entities.Summary;
import com.gerardbradshaw.mater.viewholders.StepViewViewHolder;
import com.gerardbradshaw.mater.viewmodels.DetailViewModel;
import com.gerardbradshaw.mater.viewmodels.ItemViewModel;
import com.gerardbradshaw.mater.viewmodels.SummaryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private ProgressBar progressBar;
  private LinearLayout contentView;

  private ItemViewModel itemViewModel;
  private SummaryViewModel summaryViewModel;
  private ItemListAdapter itemListAdapter;
  private RecyclerView recyclerView;

  private List<Item> itemList = new ArrayList<>();

  private List<Pair<String, List<Ingredient>>> recipeIngredientsList = new ArrayList<>();
  private List<Pair<RecyclerView, ItemListAdapter>> recyclerAndAdapterPairs = new ArrayList<>();

  private AsyncTaskScheduler taskScheduler;

  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);

    itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);

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

    // buildShoppingList();

    // Set up itemListAdapter
    itemListAdapter = new ItemListAdapter(this);
    itemListAdapter.setStockChangedListener(new ItemListAdapter.StockChangedListener() {
      @Override
      public void onStockLevelChanged(int position, int newStockLevel) {
        itemList.get(position).setStockLevel(newStockLevel);
      }
    });

    //new LoadItemsAsyncTask(progressBar, contentView, itemListAdapter).execute();

    // Set up RecyclerView
    recyclerView.setAdapter(itemListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

  }

  @Override
  protected void onPause() {
    super.onPause();
    itemViewModel.updateItem(itemList);
  }

  private void buildShoppingList() {

    // Create a runnable task
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new LoadItemsAsyncTask(progressBar, contentView, itemListAdapter).execute();
      }
    };

    taskScheduler.addNewPriorityTask(runnable);



    // Get all recipe IDs

    // Get all ingredients for each recipe and create recipeIngredientsList

    Map<Integer, Summary> summaryMap = new HashMap<>();
    List<Ingredient> ingredientList = new ArrayList<>();
    Map<Integer, List<Ingredient>> ingredientRecipeMap = new HashMap<>();

    for (Ingredient ingredient : ingredientList) {
      int recipeId = ingredient.getRecipeId();

      if (summaryMap.containsKey(recipeId)) {
        ingredientRecipeMap.get(recipeId).add(ingredient);

      } else {
        ArrayList<Ingredient> newIngredientList = new ArrayList<>();
        newIngredientList.add(ingredient);
        ingredientRecipeMap.put(recipeId, newIngredientList);
      }
    }
  }


  // - - - - - - - - - - - - - - - AsyncTasks - - - - - - - - - - - - - - -

  private class LoadItemsAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private List<Item> itemList;
    private ProgressBar progressBar;
    private LinearLayout contentView;
    private ItemListAdapter itemListAdapter;


    // Constructor
    LoadItemsAsyncTask(ProgressBar progressBar,
                       LinearLayout contentView,
                       ItemListAdapter itemListAdapter) {
      this.progressBar = progressBar;
      this.contentView = contentView;
      this.itemListAdapter = itemListAdapter;
    }


    // AsyncTask Methods
    @Override
    protected Void doInBackground(Void... voids) {
      itemList = itemViewModel.getAllItems();
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      // Update adapter
      itemListAdapter.setData(itemList);

      // Update UI
      progressBar.setVisibility(View.GONE);
      contentView.setVisibility(View.VISIBLE);

      // Save data to Activity
      ShoppingListActivity.this.itemList = itemList;
    }
  }

}
