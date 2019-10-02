package com.gerardbradshaw.mater.activities.recipedetail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.addrecipe.AddRecipeActivity;
import com.gerardbradshaw.mater.activities.main.MainActivity;
import com.gerardbradshaw.mater.pojos.IngredientHolder;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Step;
import com.gerardbradshaw.mater.viewholders.IngredientViewHolder;
import com.gerardbradshaw.mater.viewholders.StepViewViewHolder;
import com.gerardbradshaw.mater.viewmodels.ImageViewModel;
import com.gerardbradshaw.mater.viewmodels.DetailViewModel;
import com.gerardbradshaw.mater.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member Variables - - - - - - - - - - - - - - -

  private ImageViewModel imageViewModel;
  private IngredientViewModel ingredientViewModel;

  private TextView descriptionView;
  private ImageView imageView;
  private TextView servingsView;
  private Toolbar toolbar;

  private List<IngredientViewHolder> ingredientViewHolders = new ArrayList<>();
  private List<StepViewViewHolder> stepViewHolders = new ArrayList<>();
  private List<IngredientHolder> customIngredientHolders = new ArrayList<>();
  private final List<IngredientHolder> defaultIngredientHolders = new ArrayList<>();

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
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
    ingredientViewModel = ViewModelProviders.of(this).get(IngredientViewModel.class);
    DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

    // Get a handle to the Views and set up Toolbar
    imageView = findViewById(R.id.recipeDetail_image);
    descriptionView = findViewById(R.id.recipeDetail_description);
    servingsView = findViewById(R.id.recipeDetail_servings);
    toolbar = findViewById(R.id.recipeDetail_toolbar);
    setSupportActionBar(toolbar);

    // Get intent info
    recipeId = getIntent().getIntExtra(MainActivity.EXTRA_RECIPE_ID, 0);
    context = this;

    // Observe title
    detailViewModel.getLiveTitle(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        recipeTitle = s;
        toolbar.setTitle(recipeTitle);
        loadImageIntoView();
      }
    });

    // Observe image location
    imageViewModel.imageUpdateNotifier().observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(Integer integer) {
        loadImageIntoView();
      }
    });

    // Observe description
    detailViewModel.getLiveDescription(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        descriptionView.setText(s);
      }
    });

    // Observe servings
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

    // Observe ingredients
    detailViewModel.getLiveIngredients(recipeId).observe(this, new Observer<List<Ingredient>>() {
      @Override
      public void onChanged(List<Ingredient> ingredientList) {
        createIngredientHolders(ingredientList);
        ingredientListAdapter.setIngredientList(customIngredientHolders);
      }
    });

    // Observe steps
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

  private void createIngredientHolders(List<Ingredient> ingredientList) {
    defaultIngredientHolders.clear();
    customIngredientHolders.clear();

    for (Ingredient r : ingredientList) {
      String name = ingredientViewModel.getIngredient(r.getItemId()).getName();
      double amount = r.getAmount();
      String unit = r.getUnits();
      final IngredientHolder finalHolder = new IngredientHolder(name, amount, unit);
      defaultIngredientHolders.add(finalHolder);
      customIngredientHolders.add(new IngredientHolder(name, amount, unit));
    }
  }

  private void loadStepsIntoView(Step[] steps) {
    // Clear the ViewHolder references
    stepViewHolders.clear();

    // Instantiate a layout inflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point and clear it
    ViewGroup insertPoint = findViewById(R.id.recipeDetail_stepsLayout);
    insertPoint.removeAllViews();

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

    // Set up dialog for user confirmation
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    final EditText input = new EditText(this);
    input.setInputType(InputType.TYPE_CLASS_NUMBER);
    input.setHint(Integer.toString(defaultServings));
    alertBuilder.setTitle("Customize number of servings");
    alertBuilder.setView(input);

    alertBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        customServings = Integer.parseInt(input.getText().toString());
        double servingsMultiplier = customServings / defaultServings;

        // Reset customIngredientHolders
        customIngredientHolders.clear();

        // Update defaultIngredientHolders
        for (IngredientHolder holder : defaultIngredientHolders) {
          IngredientHolder customHolder = new IngredientHolder(
              holder.getName(), holder.getAmount() * servingsMultiplier, holder.getUnit());
          customIngredientHolders.add(customHolder);
        }

        ingredientListAdapter.notifyDataSetChanged();

        // Update the card view
        String servingsString = "x" + customServings;
        servingsView.setText(servingsString);
      }
    });

    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // Do nothing
      }
    });

    alertBuilder.show();
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
      // Launch AddRecipeActivity which will load the information for this recipe
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
