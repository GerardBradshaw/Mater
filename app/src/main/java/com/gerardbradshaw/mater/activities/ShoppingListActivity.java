package com.gerardbradshaw.mater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.adapters.IngredientListAdapter;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.List;

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
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Observe the LiveData
    ingredientViewModel.getLiveAllIngredients().observe(this, new Observer<List<Ingredient>>() {
      @Override
      public void onChanged(List<Ingredient> ingredients) {
        ingredientListAdapter.setIngredientList(ingredients);
      }
    });

  }
}
