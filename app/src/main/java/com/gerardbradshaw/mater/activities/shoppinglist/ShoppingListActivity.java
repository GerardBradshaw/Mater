package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.room.entities.Item;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;
  private ShoppingListAdapter shoppingListAdapter;
  private RecyclerView recyclerView;
  private List<Item> itemList = new ArrayList<>();

  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    recyclerView = findViewById(R.id.shoppingList_recycler);

    // Set up shoppingListAdapter
    shoppingListAdapter = new ShoppingListAdapter(this);

    shoppingListAdapter.setStockChangedListener(new ShoppingListAdapter.StockChangedListener() {
      @Override
      public void onStockLevelChanged(int position, Item item) {
        // Get the new item level and save it to the activity
        itemList.add(position, item);
      }
    });

    // Set up RecyclerView
    recyclerView.setAdapter(shoppingListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Observe the LiveData
    ingredientViewModel.getLiveAllIngredients().observe(this, new Observer<List<Item>>() {
      @Override
      public void onChanged(List<Item> items) {
        itemList = items;
        shoppingListAdapter.setIngredientStockList(itemList);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    ingredientViewModel.addIngredient(itemList);
  }
}
