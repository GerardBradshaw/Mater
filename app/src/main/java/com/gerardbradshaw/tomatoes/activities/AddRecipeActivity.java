package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientPojo;
import com.gerardbradshaw.tomatoes.pojos.RecipePojo;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.viewholders.AddIngredientViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.AddStepViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Layout views
  private EditText titleInput;
  private EditText descriptionInput;

  // Dynamically added view references
  private List<AddIngredientViewHolder> ingredientViewHolders;
  private List<AddStepViewHolder> stepViewHolders;

  // Data objects
  private RecipeViewModel viewModel;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Initialize the ViewModel
    viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

    // Get a handle on the views
    Button addIngredientButton = findViewById(R.id.addRecipeActivity_addIngredientButton);
    Button addStepButton = findViewById(R.id.addRecipeActivity_addStepButton);
    Button saveButton = findViewById(R.id.addRecipeActivity_saveButton);
    titleInput = findViewById(R.id.addRecipeActivity_titleInput);
    descriptionInput = findViewById(R.id.addRecipeActivity_descriptionInput);

    // Initialize the ViewHolders
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

    // Insert the view into the main view
    insertPoint.addView(addStepView, numberOfSteps, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


  }

  private void saveRecipe() {

    // Create setup fields
    boolean allFieldsOk = true;
    int promptColor = getResources().getColor(R.color.colorPrimary);

    // Check the title
    if(titleInput.getText().toString().isEmpty()) {
      titleInput.setHintTextColor(promptColor);
      allFieldsOk = false;
    }

    // Check the description
    if(descriptionInput.getText().toString().isEmpty()) {
      descriptionInput.setHintTextColor(promptColor);
      allFieldsOk = false;
    }

    // Check the ingredients
    for(AddIngredientViewHolder holder : ingredientViewHolders) {

    }





    // Create a RecipePojo object
    RecipePojo recipe = new RecipePojo();

    // Check all fields to make sure data has been entered correctly
    //boolean allFieldsOk = false;





    // Get the title and description

    recipe.setTitle(titleInput.getText().toString());
    recipe.setDescription(descriptionInput.getText().toString());

    // Set up lists for steps and ingredients
    List<RecipeIngredient> ingredients = new ArrayList<>();
    List<String> steps = new ArrayList<>();

    // Get the ingredients, amounts, and units from each and add them to the recipe
    for(AddIngredientViewHolder holder : ingredientViewHolders) {

      // Get user input
      String name = holder.getNameInput().getText().toString();
      double amount = Double.parseDouble(holder.getNameInput().getText().toString());
      RecipeIngredientPojo.Unit unit = RecipeIngredientPojo.Unit.NO_UNIT;
      // TODO implement spinner functionality and retrieval

      // Add a RecipeIngredientPojo to the recipe
      recipe.addIngredient(new RecipeIngredientPojo(name, amount, unit));
    }

    // Get the steps from each and add them to the recipe
    for(AddStepViewHolder holder : stepViewHolders) {

      // Get user input
      String step = holder.getStep().getText().toString();

      // Add the step to the recipe
      recipe.addNewStep(step);
    }

    // Save the recipe to the database
    viewModel.insertRecipeHolder(recipe);

    // TODO add a ProgressBar

  }


}
