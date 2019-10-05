package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.room.entities.Item;
import com.gerardbradshaw.mater.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private ProgressBar progressBar;
  private LinearLayout contentView;

  private ItemViewModel itemViewModel;
  private ItemListAdapter itemListAdapter;
  private RecyclerView recyclerView;
  private List<Item> itemList = new ArrayList<>();

  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
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

    // Set up itemListAdapter
    itemListAdapter = new ItemListAdapter(this);
    itemListAdapter.setStockChangedListener(new ItemListAdapter.StockChangedListener() {
      @Override
      public void onStockLevelChanged(int position, int newStockLevel) {
        itemList.get(position).setStockLevel(newStockLevel);
      }
    });

    new LoadItemsAsyncTask(progressBar, contentView, itemListAdapter).execute();

    // Set up RecyclerView
    recyclerView.setAdapter(itemListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

  }

  @Override
  protected void onPause() {
    super.onPause();
    itemViewModel.updateItem(itemList);
  }


  // - - - - - - - - - - - - - - - Load Items AsyncTask - - - - - - - - - - - - - - -

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
