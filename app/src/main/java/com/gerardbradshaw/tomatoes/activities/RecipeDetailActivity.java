package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.helpers.Units;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;
import com.gerardbradshaw.tomatoes.viewholders.RecipeIngredientViewViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.StepViewViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  // Views
  private TextView descriptionView;
  private ImageView imageView;
  private Toolbar toolbar;

  // Dynamically added view references
  private List<RecipeIngredientViewViewHolder> recipeIngredientViewHolders;
  private List<StepViewViewHolder> stepViewHolders;

  // Data objects
  private RecipeDetailViewModel viewModel;

  // Other global variables
  private int recipeId;
  private Context context;


  // - - - - - - - - - - - - - - - Activity Methods - - - - - - - - - - - - - - -
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);

    // Set up toolbar
    toolbar = findViewById(R.id.recipeDetail_toolbar);
    setSupportActionBar(toolbar);

    // Initialize the ViewModel
    viewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);

    // Get a handle to the Views
    descriptionView = findViewById(R.id.recipeDetail_summary);
    imageView = findViewById(R.id.recipeDetail_image);

    // Initialize the ViewHolders
    stepViewHolders = new ArrayList<>();
    recipeIngredientViewHolders = new ArrayList<>();

    // Get the ID of the recipe that was clicked
    Intent receivedIntent = getIntent();
    recipeId = receivedIntent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);

    // Set the context
    this.context = this;

    // Load the recipe
    loadRecipe();

  }

  private void loadRecipe() {

    // Load the image
    viewModel.getImageDirectory(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String imageDirectoryString) {
        // TODO load image from database

        Uri imageUri = Uri.parse(imageDirectoryString);

        Glide.with(context)
            .load(imageUri)
            .placeholder(context.getDrawable(R.drawable.img_placeholder_detail_view))
            .into(imageView);
      }
    });

    // Set the title
    viewModel.getTitle(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        toolbar.setTitle(s);
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
    viewModel.getIngredients(recipeId).observe(this, new Observer<RecipeIngredient[]>() {
      @Override
      public void onChanged(RecipeIngredient[] recipeIngredients) {
        loadIngredientsIntoView(recipeIngredients);
      }
    });

    // Set the steps
    viewModel.getSteps(recipeId).observe(this, new Observer<RecipeStep[]>() {
      @Override
      public void onChanged(RecipeStep[] recipeSteps) {
        loadStepsIntoView(recipeSteps);
      }
    });
  }

  private void loadIngredientsIntoView(RecipeIngredient[] recipeIngredients) {

    // Clear the ViewHolder references
    recipeIngredientViewHolders.clear();

    for(RecipeIngredient ingredient : recipeIngredients) {

      // Get the details of each ingredient
      int ingredientId = ingredient.getIngredientId();

      String name = viewModel.getIngredient(ingredientId).getName();
      String quantity = Units.formatForDetailView(ingredient.getAmount(), ingredient.getUnits());

      String ingredientDescription = quantity + name;

      // Instantiate a LayoutInflater
      LayoutInflater inflater = (LayoutInflater) getApplicationContext()
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      // Get the insert point
      ViewGroup insertPoint = findViewById(R.id.recipeDetail_ingredientsLayout);

      // Inflate the view
      LinearLayout ingredientView = (LinearLayout) inflater
          .inflate(R.layout.view_ingredient_detail, insertPoint, false);

      // Get the children of the View
      RadioButton radioButton = (RadioButton) ingredientView.getChildAt(0);
      TextView textView = (TextView) ingredientView.getChildAt(1);

      // Update the views
      textView.setText(ingredientDescription);

      // Create an ingredient view and update it
      recipeIngredientViewHolders.add(new RecipeIngredientViewViewHolder(radioButton, textView));

      // Get the index of the view
      int index = recipeIngredientViewHolders.size() - 1;

      // Insert the view into the main view
      insertPoint.addView(ingredientView,index, new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  private void loadStepsIntoView(RecipeStep[] recipeSteps) {
    // TODO load steps
  }


}
