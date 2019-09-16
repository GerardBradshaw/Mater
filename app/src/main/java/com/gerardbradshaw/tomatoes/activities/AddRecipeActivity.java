package com.gerardbradshaw.tomatoes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.tomatoes.viewholders.IngredientInputViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.StepInputViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.AddRecipeViewModel;
import com.gerardbradshaw.tomatoes.helpers.Units.MiscUnits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Layout views
  private EditText titleInput;
  private EditText descriptionInput;
  private TextView imageName;

  // Dynamically added view references
  private List<IngredientInputViewHolder> ingredientViewHolders;
  private List<StepInputViewHolder> stepViewHolders;

  // Data objects
  private AddRecipeViewModel viewModel;

  // Intents
  private static final int REQUEST_IMAGE_IMPORT = 1;

  // Logs
  private static final String LOG_TAG = "AddRecipeActivity";

  // Image
  private String imagePathString;
  private Bitmap image;


  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Initialize the ViewModel
    viewModel = ViewModelProviders.of(this).get(AddRecipeViewModel.class);

    // Get a handle on the views
    Button selectImageButton = findViewById(R.id.addRecipe_selectImageButton);
    Button addIngredientButton = findViewById(R.id.addRecipe_addIngredientButton);
    Button addStepButton = findViewById(R.id.addRecipe_addStepButton);
    Button saveButton = findViewById(R.id.addRecipe_saveButton);
    titleInput = findViewById(R.id.addRecipe_titleInput);
    descriptionInput = findViewById(R.id.addRecipe_descriptionInput);
    imageName = findViewById(R.id.addRecipe_imageNameTextView);

    // Initialize the ViewHolders
    ingredientViewHolders = new ArrayList<>();
    stepViewHolders = new ArrayList<>();

    // Set listener for selectImageButton
    selectImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        importImage();
      }
    });

    // Set listener for addIngredientButton
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredientToView();
      }
    });

    // Set listener for addStepButton
    addStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addStepToView();
      }
    });

    // Set listener for saveButton
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveRecipeToRepository();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    if (requestCode == REQUEST_IMAGE_IMPORT &&
        resultCode == RESULT_OK &&
        data != null) {

      // Get the URI of the imageName and add it to the app.
      // Image
      Uri imageUri = data.getData();
      assert imageUri != null;

      // Set the view
      imageName.setText(getFileName(imageUri));

      // Save the image uri
      imagePathString = imageUri.toString();
      //importImageFromUri(imageUri);

    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  // - - - - - - - - - - - - - - - Button clicked methods - - - - - - - - - - - - - - -

  private void importImage() {
    // Create the intent to import an imageName
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

    // Filter files only to those that can be "opened" and directly accessed as a stream.
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Only show images.
    intent.setType("image/*");

    startActivityForResult(intent, REQUEST_IMAGE_IMPORT);
  }

  private void addIngredientToView() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipe_addIngredientLayout);

    // Inflate the view
    LinearLayout addIngredientView =
        (LinearLayout) inflater.inflate(R.layout.view_ingredient_input, insertPoint, false);

    // Get the children of the View
    EditText nameInput = (EditText) addIngredientView.getChildAt(0);
    EditText amountInput = (EditText) addIngredientView.getChildAt(1);
    Spinner unitsSpinner = (Spinner) addIngredientView.getChildAt(2);

    // Save the new views to the list
    ingredientViewHolders.add(new IngredientInputViewHolder(nameInput, amountInput, unitsSpinner));

    // Get the index of the view
    int index = ingredientViewHolders.size() - 1;

    // Insert the view into main view
    insertPoint.addView(addIngredientView,index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void addStepToView() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipe_addStepLayout);

    // Inflate the view
    LinearLayout addStepView =
        (LinearLayout) inflater.inflate(R.layout.view_step_input, insertPoint, false);

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
    stepViewHolders.add(new StepInputViewHolder(number, step));

    // Insert the view into the main view
    insertPoint.addView(addStepView, numberOfSteps, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


  }

  private void saveRecipeToRepository() {

    // Create setup fields
    boolean allFieldsOk = true;
    int hintColor = getResources().getColor(R.color.colorPrimary);

    // Check the title
    if(titleInput.getText().toString().isEmpty()) {
      titleInput.setHintTextColor(hintColor);
      allFieldsOk = false;
    }

    // Check the description
    if(descriptionInput.getText().toString().isEmpty()) {
      descriptionInput.setHintTextColor(hintColor);
      allFieldsOk = false;
    }

    // Check the ingredients
    for(IngredientInputViewHolder holder : ingredientViewHolders) {

      if(holder.getNameInput().getText().toString().isEmpty()) {
        holder.getNameInput().setHintTextColor(hintColor);
        allFieldsOk = false;
      }

      if(holder.getAmountInput().getText().toString().isEmpty()) {
        holder.getAmountInput().setHintTextColor(hintColor);
        allFieldsOk = false;
      }

      // TODO add checker for spinner
    }

    // Check the steps
    for(StepInputViewHolder holder : stepViewHolders) {
      if(holder.getStep().getText().toString().isEmpty()) {
        holder.getStep().setHintTextColor(hintColor);
        allFieldsOk = false;
      }
    }

    // If all is well, add the recipe to the database
    if(allFieldsOk) {

      // Create a RecipeHolder object
      RecipeHolder recipe = new RecipeHolder();

      // Set up lists for steps and ingredients
      List<RecipeIngredientHolder> ingredients = new ArrayList<>();
      List<String> steps = new ArrayList<>();

      // Get the ingredients info from each ViewHolder and add them to the list
      for(IngredientInputViewHolder holder : ingredientViewHolders) {
        ingredients.add(new RecipeIngredientHolder(
            holder.getNameInput().getText().toString(),
            Double.parseDouble(holder.getAmountInput().getText().toString()),
            MiscUnits.NO_UNIT));

        // TODO implement spinner functionality and retrieval
      }

      // Get the steps from each ViewHolder and add them to the list
      for(StepInputViewHolder holder : stepViewHolders) {
        steps.add(holder.getStep().getText().toString());
      }

      // Add everything to the recipe
      recipe.setTitle(titleInput.getText().toString());
      recipe.setDescription(descriptionInput.getText().toString());
      recipe.setRecipeIngredients(ingredients);
      recipe.setSteps(steps);

      // Save the recipe to the database
      viewModel.insertRecipeHolder(recipe);

      // Save the image
      if (image != null) {
        viewModel.saveImage(recipe.getTitle(), image);
      }

      finish();

      // TODO add a ProgressBar

    } else {
      Toast
          .makeText(this, "Please complete indicated fields", Toast.LENGTH_SHORT)
          .show();
    }
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void importImageFromUri(@NonNull Uri uri) {
    try {
      // Use the MediaStore to load the imageName.
      Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

      if (image != null) {

        this.image = image;
        // Tell the presenter to import this imageName.
        // TODO the imageName has been imported as a bitmap.
        //  Add the imageName to the database using the repo.
        //mPresenter.onImportImage(imageName);

      }
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(LOG_TAG, "Error: " + e.getMessage() + "Could not open URI: "
          + uri.toString());
    }
  }

  public String getFileName(@NonNull Uri uri) {
    String result = null;
    if (Objects.requireNonNull(uri.getScheme()).equals("content")) {
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        assert cursor != null;
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      assert result != null;
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }

}
