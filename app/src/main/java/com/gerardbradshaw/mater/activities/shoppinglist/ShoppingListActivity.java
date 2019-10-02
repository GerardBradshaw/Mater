package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.room.entities.Item;
import com.gerardbradshaw.mater.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

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

    // Set up itemListAdapter
    itemListAdapter = new ItemListAdapter(this);

    itemListAdapter.setStockChangedListener(new ItemListAdapter.StockChangedListener() {
      @Override
      public void onStockLevelChanged(int position, Item item) {
        // Get the new item level and save it to the activity
        itemList.add(position, item);
      }
    });

    //new LoadItemsAsyncTask()

    // Set up RecyclerView
    recyclerView.setAdapter(itemListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Observe the LiveData
    itemViewModel.getLiveAllItems().observe(this, new Observer<List<Item>>() {
      @Override
      public void onChanged(List<Item> items) {
        itemList = items;
        itemListAdapter.setData(itemList);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    itemViewModel.addItem(itemList);
  }


  // - - - - - - - - - - - - - - - Load Items AsyncTask - - - - - - - - - - - - - - -

  private class LoadItemsAsyncTask extends AsyncTask<Void, Void, Void> {

    // Member variables
    private List<Item> itemList;
    private ProgressBar progressBar;
    private NestedScrollView contentScrollView;
    private ItemListAdapter itemListAdapter;


    // Constructor
    LoadItemsAsyncTask(ProgressBar progressBar,
                       NestedScrollView contentScrollView,
                       ItemListAdapter itemListAdapter) {
      this.progressBar = progressBar;
      this.contentScrollView = contentScrollView;
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
      contentScrollView.setVisibility(View.VISIBLE);

      // Save data to Activity
      ShoppingListActivity.this.itemList = itemList;
    }
  }
}
