package com.gerardbradshaw.mater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.adapters.IngredientListAdapter;
import com.gerardbradshaw.mater.helpers.SharedPrefHelper;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;
  private IngredientListAdapter ingredientListAdapter;
  private RecyclerView recyclerView;
  private List<Ingredient> ingredientList = new ArrayList<>();
  private SharedPrefHelper sharedPrefHelper;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    recyclerView = findViewById(R.id.shoppingList_recycler);

    // Set up ingredientListAdapter and recyclerView
    ingredientListAdapter = new IngredientListAdapter(this);
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Set up SharedPrefHelper
    sharedPrefHelper = new SharedPrefHelper(this);

    // Observe the LiveData
    ingredientViewModel.getLiveAllIngredients().observe(this, new Observer<List<Ingredient>>() {
      @Override
      public void onChanged(List<Ingredient> ingredients) {
        ingredientList = ingredients;
        ingredientListAdapter.setIngredientList(ingredients);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    // Save ingredient states to shared preferences
    for (Ingredient ingredient : ingredientList) {
      // TODO add each ingredient as the key in shared preferences with a boolean for in stock or out of stock

    }


  }
}
