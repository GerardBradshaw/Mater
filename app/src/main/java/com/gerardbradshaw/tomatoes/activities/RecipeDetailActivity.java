package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gerardbradshaw.tomatoes.R;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  private TextView titleView;
  private TextView summaryView;
  private TextView ingredientsView;
  private TextView stepsView;
  private int recipeId;



  // - - - - - - - - - - - - - - - Activity Methods - - - - - - - - - - - - - - -
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);

    // Get a handle to the Views
    titleView = findViewById(R.id.recipeDetail_title);
    summaryView = findViewById(R.id.recipeDetail_summary);
    ingredientsView = findViewById(R.id.recipeDetailActivity_ingredients);
    stepsView = findViewById(R.id.recipeDetailActivity_steps);

    // Get a handle to the ViewModel


    // Get the ID of the recipe that was clicked
    Intent receivedIntent = getIntent();
    recipeId = receivedIntent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);

    // Get the RecipePojo information from the database



  }
}
