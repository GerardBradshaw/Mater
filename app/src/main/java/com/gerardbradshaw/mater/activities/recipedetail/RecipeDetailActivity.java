package com.gerardbradshaw.mater.activities.recipedetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.AddRecipeActivity;
import com.gerardbradshaw.mater.activities.main.MainActivity;
import com.gerardbradshaw.mater.helpers.Units;
import com.gerardbradshaw.mater.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.mater.room.entities.RecipeIngredient;
import com.gerardbradshaw.mater.room.entities.Step;
import com.gerardbradshaw.mater.viewholders.RecipeIngredientViewHolder;
import com.gerardbradshaw.mater.viewholders.StepViewViewHolder;
import com.gerardbradshaw.mater.viewmodels.ImageViewModel;
import com.gerardbradshaw.mater.viewmodels.DetailViewModel;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  private DetailViewModel detailViewModel;
  private ImageViewModel imageViewModel;
  private IngredientViewModel ingredientViewModel;

  private TextView descriptionView;
  private ImageView imageView;
  private TextView servingsView;
  private Toolbar toolbar;

  private List<RecipeIngredientViewHolder> recipeIngredientViewHolders = new ArrayList<>();
  private List<StepViewViewHolder> stepViewHolders = new ArrayList<>();
  private List<RecipeIngredientHolder> recipeIngredientHolders = new ArrayList<>();

  private Context context;
  private String recipeTitle;
  private int recipeId;
  private int customServings = 1;
  private int defaultServings;

  private IngredientListAdapter ingredientListAdapter;

  private static String LOG_TAG = "GGG - RecipeDetailActivity";
  public static final String EXTRA_RECIPE_ID = "com.gerardbradshaw.mater.EXTRA_RECIPE_ID";


  // - - - - - - - - - - - - - - - Activity Methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);

    // Get intent information
    Intent receivedIntent = getIntent();
    recipeId = receivedIntent.getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);
    context = this;

    // Get a handle to the Views
    imageView = findViewById(R.id.recipeDetail_image);
    descriptionView = findViewById(R.id.recipeDetail_description);
    servingsView = findViewById(R.id.recipeDetail_servings);
    toolbar = findViewById(R.id.recipeDetail_toolbar);
    setSupportActionBar(toolbar);

    // Set the title
    detailViewModel.getLiveTitle(recipeId).observe(this, new Observer<String>() {
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
    detailViewModel.getLiveDescription(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        descriptionView.setText(s);
      }
    });

    detailViewModel.getLiveServings(recipeId).observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(Integer i) {
        defaultServings = i;
        String servingsString = "x" + i;
        servingsView.setText(servingsString);
      }
    });

    // Set up RecyclerView for ingredients
    ingredientListAdapter = new IngredientListAdapter(this);
    RecyclerView recyclerView = findViewById(R.id.recipeDetail_recyclerView);
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Set the ingredients
    detailViewModel.getLiveRecipeIngredients(recipeId).observe(this, new Observer<List<RecipeIngredient>>() {
      @Override
      public void onChanged(List<RecipeIngredient> recipeIngredientList) {
        createRecipeIngredientHolders(recipeIngredientList);
        ingredientListAdapter.setSummaryList(recipeIngredientHolders);
        //loadIngredientsIntoView();
      }
    });

    // Set the steps
    detailViewModel.getLiveSteps(recipeId).observe(this, new Observer<Step[]>() {
      @Override
      public void onChanged(Step[] steps) {
        loadStepsIntoView(steps);
      }
    });
  }


  // - - - - - - - - - - - - - - - Helper Methods - - - - - - - - - - - - - - -

  private void loadImageIntoView() {
    Glide.with(context)
        .load(imageViewModel.getFile(recipeTitle))
        .placeholder(context.getDrawable(R.drawable.img_placeholder_detail_view))
        .into(imageView);
  }

  private void createRecipeIngredientHolders(List<RecipeIngredient> recipeIngredientList) {
    recipeIngredientViewHolders.clear();

    for (RecipeIngredient r : recipeIngredientList) {
      String name = ingredientViewModel.getIngredient(r.getIngredientId()).getName();
      double amount = r.getAmount();
      String unit = r.getUnits();
      recipeIngredientHolders.add(new RecipeIngredientHolder(name, amount, unit));
    }
  }

  private void loadIngredientsIntoView() {
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    ViewGroup insertPoint = findViewById(R.id.recipeDetail_ingredientsLayout);

    // Add a new View to the layout for each ingredient
    for(RecipeIngredientHolder holder : recipeIngredientHolders) {
      // Inflate the view to be inserted
      LinearLayout ingredientView = (LinearLayout) inflater
          .inflate(R.layout.ingredient_detail, insertPoint, false);

      // Get the children of the View
      CheckBox checkBox = (CheckBox) ingredientView.getChildAt(0);
      TextView quantityView = (TextView) ingredientView.getChildAt(1);
      TextView nameView = (TextView) ingredientView.getChildAt(2);

      // Format the amount and set the View String
      String quantity = Units.formatForDetailView(holder.getAmount(), holder.getUnit());

      // Update the views
      quantityView.setText(quantity);
      nameView.setText(holder.getName());

      // Create an ingredient view and update it
      recipeIngredientViewHolders.add(new RecipeIngredientViewHolder(checkBox, quantityView, nameView));

      // Get the index of the view
      int index = recipeIngredientViewHolders.size() - 1;

      // Insert the view into the main view
      insertPoint.addView(ingredientView, index, new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

      ingredientView.invalidate();
    }
  }

  private void loadStepsIntoView(Step[] steps) {
    // Clear the ViewHolder references
    stepViewHolders.clear();

    // Instantiate a layout inflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.recipeDetail_stepsLayout);

    for (Step step : steps) {
      String description = step.getDescription();
      int number = step.getNumber();

      // Inflate the view to be inserted
      LinearLayout stepView = (LinearLayout) inflater
          .inflate(R.layout.step_detail, insertPoint, false);

      // Get the children of the view
      TextView numberView = (TextView) stepView.getChildAt(0);
      TextView descriptionView = (TextView) stepView.getChildAt(1);

      // Update the views
      String numberViewString = number + ". ";
      numberView.setText(numberViewString);
      descriptionView.setText(description);

      // Create a step view and update it
      stepViewHolders.add(new StepViewViewHolder(numberView, descriptionView));

      // Get the index of the view
      int index = stepViewHolders.size() - 1;

      // Insert the view into the main view
      insertPoint.addView(stepView, index, new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  private void updateServings() {
    // TODO Display dialog for the user to enter the number of servings

    // Save the amount to the class
    this.customServings = 1;
    double servingsMultiplier = customServings / defaultServings;

    // Update holders
    for (RecipeIngredientHolder holder : recipeIngredientHolders) {
      holder.setAmount(holder.getAmount() * servingsMultiplier);
    }

    ingredientListAdapter.notifyDataSetChanged();

    // Update the card view
    String servingsString = "x" + customServings;
    servingsView.setText(servingsString);
  }


  // - - - - - - - - - - - - - - - Options Menu methods - - - - - - - - - - - - - - -

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.recipe_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_edit) {
      Intent intent = new Intent(RecipeDetailActivity.this, AddRecipeActivity.class);
      intent.putExtra(EXTRA_RECIPE_ID, recipeId);
      ActivityOptionsCompat options =
          ActivityOptionsCompat.makeSceneTransitionAnimation(RecipeDetailActivity.this);
      startActivity(intent, options.toBundle());

    } else if (id == R.id.action_servings) {
      updateServings();
    }

    return super.onOptionsItemSelected(item);
  }

}
