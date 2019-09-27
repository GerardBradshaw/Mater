package com.gerardbradshaw.mater.activities.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;
  private ShoppingListAdapter shoppingListAdapter;
  private RecyclerView recyclerView;
  private List<Ingredient> ingredientList = new ArrayList<>();

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
      public void onStockLevelChanged(int position, Ingredient ingredient) {
        // Get the new ingredient level and save it to the activity
        ingredientList.add(position, ingredient);
      }
    });

    // Set up RecyclerView
    recyclerView.setAdapter(shoppingListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Observe the LiveData
    ingredientViewModel.getLiveAllIngredients().observe(this, new Observer<List<Ingredient>>() {
      @Override
      public void onChanged(List<Ingredient> ingredients) {
        ingredientList = ingredients;
        shoppingListAdapter.setIngredientStockList(ingredientList);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    ingredientViewModel.addIngredient(ingredientList);
  }
}
