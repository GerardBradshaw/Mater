package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.helpers.Units;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;
import com.gerardbradshaw.tomatoes.viewholders.RecipeIngredientViewViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.StepViewViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.ImageViewModel;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  private TextView descriptionView;
  private ImageView imageView;
  private Toolbar toolbar;

  private List<RecipeIngredientViewViewHolder> recipeIngredientViewHolders;
  private List<StepViewViewHolder> stepViewHolders;

  private RecipeDetailsViewModel detailsViewModel;
  private ImageViewModel imageViewModel;
  private RecipeRepository repository;

  private int recipeId;
  private Context context;

  private static String LOG_TAG = "GGG - RecipeDetailActivity";

  private String recipeTitle;


  // - - - - - - - - - - - - - - - Activity Methods - - - - - - - - - - - - - - -
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);

    // Get the ID of the recipe that was clicked
    Intent receivedIntent = getIntent();
    recipeId = receivedIntent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);

    // Set the context
    context = this;

    // Initialize the VMs
    detailsViewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

    // Get the title
    recipeTitle = detailsViewModel.getTitle(recipeId).getValue();

    // Set up Toolbar
    toolbar = findViewById(R.id.recipeDetail_toolbar);
    setSupportActionBar(toolbar);

    // Get a handle to the Views and initialize the dynamic View holders
    imageView = findViewById(R.id.recipeDetail_image);
    descriptionView = findViewById(R.id.recipeDetail_summary);
    stepViewHolders = new ArrayList<>();
    recipeIngredientViewHolders = new ArrayList<>();

    // Set the title
    detailsViewModel.getTitle(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        recipeTitle = s;
        toolbar.setTitle(recipeTitle);
        loadImageIntoView();
      }
    });

    // Load the image
    imageViewModel.imageUpdateNotifier().observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(Integer integer) {
        loadImageIntoView();
      }
    });

    // Set the description
    detailsViewModel.getDescription(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        descriptionView.setText(s);
      }
    });

    // Set the ingredients
    detailsViewModel.getIngredients(recipeId).observe(this, new Observer<RecipeIngredient[]>() {
      @Override
      public void onChanged(RecipeIngredient[] recipeIngredients) {
        loadIngredientsIntoView(recipeIngredients);
      }
    });

    // Set the steps
    detailsViewModel.getSteps(recipeId).observe(this, new Observer<RecipeStep[]>() {
      @Override
      public void onChanged(RecipeStep[] recipeSteps) {
        loadStepsIntoView(recipeSteps);
      }
    });
  }

  private void loadImageIntoView() {
    Glide.with(context)
        .load(imageViewModel.getFile(recipeTitle))
        .placeholder(context.getDrawable(R.drawable.img_placeholder_detail_view))
        .into(imageView);
  }

  private void loadIngredientsIntoView(RecipeIngredient[] recipeIngredients) {

    // Clear the ViewHolder references
    recipeIngredientViewHolders.clear();

    for(RecipeIngredient ingredient : recipeIngredients) {

      // Get the details of each ingredient
      int ingredientId = ingredient.getIngredientId();

      String name = detailsViewModel.getIngredient(ingredientId).getName();
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
