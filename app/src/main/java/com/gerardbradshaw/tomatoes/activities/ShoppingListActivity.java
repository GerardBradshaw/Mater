package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.viewmodels.IngredientViewModel;

public class ShoppingListActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientViewModel ingredientViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_list);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    

  }
}
