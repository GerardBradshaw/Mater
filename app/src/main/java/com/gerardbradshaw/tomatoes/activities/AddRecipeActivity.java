package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.holders.RecipeHolder;
import com.gerardbradshaw.tomatoes.holders.RecipeIngredientHolder;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.viewholders.AddIngredientViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.AddStepViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Default layout views
  private Button addIngredientButton;
  private Button addStepButton;
  private Button saveButton;
  private EditText titleInput;
  private EditText descriptionInput;

  // Dynamically added view references
  private List<AddIngredientViewHolder> ingredientViewHolders;
  private List<AddStepViewHolder> stepViewHolders;




  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Get a handle on the views
    addIngredientButton = findViewById(R.id.addRecipeActivity_addIngredientButton);
    addStepButton = findViewById(R.id.addRecipeActivity_addStepButton);
    saveButton = findViewById(R.id.addRecipeActivity_saveButton);
    titleInput = findViewById(R.id.addRecipeActivity_titleInput);
    descriptionInput = findViewById(R.id.addRecipeActivity_descriptionInput);

    // Initialize the view holders
    ingredientViewHolders = new ArrayList<>();
    stepViewHolders = new ArrayList<>();

    // Set listener for addIngredientButton
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredient();
      }
    });

    // Set listener for addStepButton
    addStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addStep();
      }
    });

    // Set listener for saveButton
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveRecipe();
      }
    });

  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void addIngredient() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipeActivity_addIngredientLayout);

    // Inflate the view
    LinearLayout addIngredientView =
        (LinearLayout) inflater.inflate(R.layout.add_ingredient, insertPoint, false);

    // Get the children of the View
    EditText nameInput = (EditText) addIngredientView.getChildAt(0);
    EditText amountInput = (EditText) addIngredientView.getChildAt(1);
    Spinner unitsSpinner = (Spinner) addIngredientView.getChildAt(2);

    // Save the new views to the list
    ingredientViewHolders.add(new AddIngredientViewHolder(nameInput, amountInput, unitsSpinner));

    // Get the index of the view
    int index = ingredientViewHolders.size() - 1;

    // Insert the view into main view
    insertPoint.addView(addIngredientView,index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void addStep() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipeActivity_addStepLayout);

    // Inflate the view
    LinearLayout addStepView =
        (LinearLayout) inflater.inflate(R.layout.add_step, insertPoint, false);

    // Get the children of the View
    TextView number = (TextView) addStepView.getChildAt(0);
    EditText step = (EditText) addStepView.getChildAt(1);

    // Get the numberOfSteps of the view and the index
    int numberOfSteps = stepViewHolders.size();
    String numberOfStepsString;

    if(numberOfSteps < 1) {
      numberOfStepsString = "1. ";

    } else {
      int stepNumber = numberOfSteps + 1;
      numberOfStepsString = stepNumber + ". ";
    }

    // Set the text of the step number
    number.setText(numberOfStepsString);

    // Save the new EditText to the list
    stepViewHolders.add(new AddStepViewHolder(number, step));

    // Get the index of the view
    int index = numberOfSteps;

    // Insert the view into the main view
    insertPoint.addView(addStepView, index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


  }

  private void saveRecipe() {

    // Create a RecipeHolder object
    RecipeHolder recipe = new RecipeHolder();

    // Get the title and description
    recipe.setTitle(titleInput.getText().toString());
    recipe.setDescription(descriptionInput.getText().toString());

    // Set up holders for steps and ingredients
    List<RecipeIngredient> ingredients = new ArrayList<>();
    List<String> steps = new ArrayList<>();

    // Get the ingredients, amounts, and units from each and add them to the recipe
    for(AddIngredientViewHolder holder : ingredientViewHolders) {

      // Get user input
      String name = holder.getNameInput().getText().toString();
      double amount = Double.parseDouble(holder.getNameInput().getText().toString());
      RecipeIngredientHolder.Unit unit = RecipeIngredientHolder.Unit.NO_UNIT;
      // TODO implement spinner functionality and retrieval

      // Add a RecipeIngredientHolder to the recipe
      recipe.addIngredient(new RecipeIngredientHolder(name, amount, unit));
    }

    // Get the steps from each and add them to the recipe
    for(AddStepViewHolder holder : stepViewHolders) {

      // Get user input
      String step = holder.getStep().getText().toString();

      // Add the step to the recipe
      recipe.addNewStep(step);
    }

    // TODO save the recipe to the database using a VH

    // TODO add a ProgressBar

  }


}
