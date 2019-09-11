package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;
import com.gerardbradshaw.tomatoes.viewholders.RecipeIngredientViewViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.StepViewViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeDetailViewModel;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  // Views
  private TextView titleView;
  private TextView descriptionView;

  // Dynamically added view references
  private List<StepViewViewHolder> stepViewHolders;
  private List<RecipeIngredientViewViewHolder> recipeIngredientViewHolders;

  // Data objects
  private RecipeDetailViewModel viewModel;

  // Other global variables
  private int recipeId;


  // - - - - - - - - - - - - - - - Activity Methods - - - - - - - - - - - - - - -
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);

    // Initialize the ViewModel
    viewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);

    // Get a handle to the Views
    titleView = findViewById(R.id.recipeDetail_title);
    descriptionView = findViewById(R.id.recipeDetail_summary);

    // Initialize the ViewHolders
    stepViewHolders = new ArrayList<>();
    recipeIngredientViewHolders = new ArrayList<>();

    // Get the ID of the recipe that was clicked
    Intent receivedIntent = getIntent();
    recipeId = receivedIntent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);

    // Load the recipe
    loadRecipe();

  }

  private void loadRecipe() {

    // Set the title
    viewModel.getTitle(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        titleView.setText(s);
      }
    });

    // Set the description
    viewModel.getDescription(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        descriptionView.setText(s);
      }
    });

    // Set the ingredients


  }
}
