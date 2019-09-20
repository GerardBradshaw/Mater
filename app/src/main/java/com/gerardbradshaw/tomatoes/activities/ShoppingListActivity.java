package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.activities.adapters.IngredientListAdapter;
import com.gerardbradshaw.tomatoes.viewmodels.IngredientViewModel;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;
  private IngredientListAdapter ingredientListAdapter;
  private RecyclerView recyclerView;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    recyclerView = findViewById(R.id.shoppingList_recycler);

    // Set up ingredientListAdapter and recyclerView
    ingredientListAdapter = new IngredientListAdapter(this);
    ingredientListAdapter.setIngredientList(ingredientViewModel.getAllIngredients());
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

  }
}
