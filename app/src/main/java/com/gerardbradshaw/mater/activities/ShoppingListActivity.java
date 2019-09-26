package com.gerardbradshaw.mater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CheckBox;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.adapters.IngredientListAdapter;
import com.gerardbradshaw.mater.helpers.SharedPrefHelper;
import com.gerardbradshaw.mater.pojos.StockHolder;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;
  private IngredientListAdapter ingredientListAdapter;
  private RecyclerView recyclerView;
  private SharedPrefHelper sharedPrefHelper;
  private List<StockHolder> stockHolders;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    recyclerView = findViewById(R.id.shoppingList_recycler);

    // Set up ingredientListAdapter
    ingredientListAdapter = new IngredientListAdapter(this);

    ingredientListAdapter.setIngredientClickedListener(new IngredientListAdapter.IngredientClickedListener() {
      @Override
      public void onIngredientClicked(StockHolder stockHolder) {
        // TODO save the checkbox status to a member variable
      }
    });

    // Set up RecyclerView
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Set up SharedPrefHelper
    sharedPrefHelper = new SharedPrefHelper(this);

    // Observe the LiveData
    ingredientViewModel.getLiveAllIngredients().observe(this, new Observer<List<Ingredient>>() {
      @Override
      public void onChanged(List<Ingredient> ingredients) {
        // Add each ingredient to the list
        for (Ingredient ingredient : ingredients) {
          boolean inStock = sharedPrefHelper.getBoolean(ingredient.getName(), false);
          StockHolder stockHolder = new StockHolder(ingredient, inStock);
          stockHolders.add(stockHolder);
        }

        // Update the adapter
        ingredientListAdapter.setIngredientStockList(stockHolders);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    // TODO Save stock levels to database
  }
}
