package com.gerardbradshaw.mater.activities.addrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.recipedetail.RecipeDetailActivity;
import com.gerardbradshaw.mater.pojos.IngredientHolder;
import com.gerardbradshaw.mater.pojos.RecipeHolder;
import com.gerardbradshaw.mater.helpers.Units.MiscUnits;
import com.gerardbradshaw.mater.viewmodels.ImageViewModel;
import com.gerardbradshaw.mater.viewmodels.DetailViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private DetailViewModel detailViewModel;
  private ImageViewModel imageViewModel;

  private ProgressBar progressBar;
  private NestedScrollView contentScrollView;
  private EditText titleInput;
  private EditText descriptionInput;
  private EditText servingsInput;
  private TextView imageNameView;
  private Bitmap image;

  private List<String> stepHolders = new ArrayList<>();
  private List<IngredientHolder> ingredientHolders = new ArrayList<>();

  private AddIngredientListAdapter ingredientListAdapter;
  private AddStepListAdapter stepListAdapter;

  private static final int REQUEST_IMAGE_IMPORT = 1;
  private static final String LOG_TAG = "GGG - AddRecipeActivity";


  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

    // Get a handle on the views
    progressBar = findViewById(R.id.addRecipe_progressBar);
    contentScrollView = findViewById(R.id.addRecipe_contentScrollView);
    titleInput = findViewById(R.id.addRecipe_titleInput);
    descriptionInput = findViewById(R.id.addRecipe_descriptionInput);
    servingsInput = findViewById(R.id.addRecipe_servingsInput);
    imageNameView = findViewById(R.id.addRecipe_imageNameTextView);

    // Set up Toolbar
    Toolbar toolbar = findViewById(R.id.addRecipe_toolbar);

    // Set up Ingredient RecyclerView
    ingredientListAdapter = new AddIngredientListAdapter(this);
    ingredientListAdapter.setData(ingredientHolders);
    RecyclerView ingredientRecyclerView = findViewById(R.id.addRecipe_ingredientRecyclerView);
    ingredientRecyclerView.setAdapter(ingredientListAdapter);
    ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    addIngredientToRecycler();

    // Set up Step RecyclerView
    stepListAdapter = new AddStepListAdapter(this);
    stepListAdapter.setData(stepHolders);
    RecyclerView stepRecyclerView = findViewById(R.id.addRecipe_stepRecyclerView);
    stepRecyclerView.setAdapter(stepListAdapter);
    stepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    addStepToRecycler();

    // Pre-fill data if loading from existing recipe
    int recipeId = getIntent().getIntExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, 0);
    if (recipeId != 0) {
      toolbar.setTitle(getString(R.string.add_recipe_header_edit));
      setSupportActionBar(toolbar);
      loadExistingRecipe(recipeId);
    } else {
      toolbar.setTitle(getString(R.string.add_recipe_header));
      setSupportActionBar(toolbar);
    }

    // Listen for edited ingredient names
    ingredientListAdapter.setNameEditedListener(
        new AddIngredientListAdapter.NameEditedListener() {
      @Override
      public void onNameEdited(int position, String newName) {
        ingredientHolders.get(position).setName(newName);
      }
    });

    // Listen for edited ingredient amounts
    ingredientListAdapter.setAmountEditedListener(
        new AddIngredientListAdapter.AmountEditedListener() {
      @Override
      public void onAmountEdited(int position, double amount) {
        ingredientHolders.get(position).setAmount(amount);
      }
    });

    stepListAdapter.setStepEditedListener(new AddStepListAdapter.StepEditedListener() {
      @Override
      public void onDescriptionEdited(int position, String newDescription) {
        stepHolders.set(position, newDescription);
      }
    });

    // Set listener for selectImageButton
    Button selectImageButton = findViewById(R.id.addRecipe_selectImageButton);
    selectImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        importImage();
      }
    });

    // Set listener for addIngredientButton
    Button addIngredientButton = findViewById(R.id.addRecipe_addIngredientButton);
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredientToRecycler();
      }
    });

    // Set listener for addStepButton
    Button addStepButton = findViewById(R.id.addRecipe_addStepButton);
    addStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addStepToRecycler();
      }
    });

    // Set listener for saveButton
    Button saveButton = findViewById(R.id.addRecipe_saveButton);
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveRecipeToRepository();
      }
    });

    // Set up cancel button
    Button cancelButton = findViewById(R.id.addRecipe_cancel);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showCancelDialog();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    if (requestCode == REQUEST_IMAGE_IMPORT &&
        resultCode == RESULT_OK &&
        data != null) {

      // Get the URI of the imageName and add it to the app.
      Uri imageUri = data.getData();
      assert imageUri != null;

      // Set the view
      imageNameView.setText(getFileName(imageUri));

      importImageFromUri(imageUri);

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

  private void addIngredientToRecycler() {
    ingredientHolders.add(new IngredientHolder());
    ingredientListAdapter.notifyDataSetChanged();
  }

  private void addStepToRecycler() {
    stepHolders.add("");
    stepListAdapter.notifyDataSetChanged();
  }

  private void saveRecipeToRepository() {
    // Create setup fields
    boolean allFieldsOk = true;
    int hintColor = getResources().getColor(R.color.colorPrimary);

    // Check title
    if (titleInput.getText().toString().isEmpty()) {
      showToast("Please add a title.");
      titleInput.setHintTextColor(hintColor);
      allFieldsOk = false;
      Log.d(LOG_TAG, "Cannot save because of title");
    }

    // Check servings
    if (servingsInput.getText().toString().isEmpty()) {
      showToast("Please add the number of servings.");
      servingsInput.setHintTextColor(hintColor);
      allFieldsOk = false;
      Log.d(LOG_TAG, "Cannot save because of servings");
    }

    // Check description
    if(descriptionInput.getText().toString().isEmpty()) {
      showToast("Please add a description.");
      descriptionInput.setHintTextColor(hintColor);
      allFieldsOk = false;
      Log.d(LOG_TAG, "Cannot save because of description");

    }

    // Check ingredients
    for(IngredientHolder holder : ingredientHolders) {

      if(holder.getName().isEmpty()) {
        showToast("Please add names for each ingredient.");
        allFieldsOk = false;
        Log.d(LOG_TAG, "Cannot save because of an ingredient name");
      }

      if(holder.getAmount() == 0) {
        showToast("Please add amounts for each ingredient.");
        allFieldsOk = false;
        Log.d(LOG_TAG, "Cannot save because of an ingredient amount");
      }

      // TODO add checker for spinner
    }

    // Check the steps
    if (stepHolders.isEmpty()
        || (stepHolders.get(0).isEmpty() && stepHolders.size() == 1)) {
      showToast("Please add at least 1 step");
      allFieldsOk = false;
      Log.d(LOG_TAG, "Cannot save because there are no steps");

    } else {

      for (int i = 0; i < stepHolders.size(); i++) {
        if (stepHolders.get(i).isEmpty()) {
          stepHolders.remove(i);
          i--;
          Log.d(LOG_TAG, "Blank step removed");
        }
      }
    }

    // If all is well, add the recipe to the database
    if(allFieldsOk) {

      // Create a RecipeHolder object
      RecipeHolder recipe = new RecipeHolder();

      // Clean up each IngredientHolder in the Activity
      for(IngredientHolder holder : ingredientHolders) {
        // TODO implement retrieval from spinner

        if (holder.getUnit().isEmpty()) {
          holder.setUnit(MiscUnits.NO_UNIT.name());
        }
      }

      // Add everything to the recipe
      recipe.setTitle(titleInput.getText().toString());
      recipe.setDescription(descriptionInput.getText().toString());
      recipe.setServings(Integer.parseInt(servingsInput.getText().toString()));
      recipe.setImageDirectory(imageNameView.getText().toString());
      recipe.setIngredientHolders(ingredientHolders);
      recipe.setSteps(stepHolders);

      // Save the recipe to the database
      detailViewModel.insertRecipeHolder(recipe);

      // Save the image
      if (image != null) {
        imageViewModel.saveImage(recipe.getTitle(), image);
      }

      // End the activity
      finish();
    }
  }

  private void showCancelDialog() {
    // Set up dialog for user confirmation
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddRecipeActivity.this);
    alertBuilder.setMessage(getString(R.string.add_recipe_dialog_discard_changes));

    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        finish();
      }
    });

    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // Do nothing
      }
    });

    alertBuilder.show();
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void loadExistingRecipe(int recipeId) {

    // Start AsyncTask to load RecipeHolder for recipe
    new LoadRecipeAsyncTask(progressBar, contentScrollView, titleInput, servingsInput,
        descriptionInput, imageNameView, ingredientListAdapter, stepListAdapter).execute(recipeId);

  }

  private void importImageFromUri(@NonNull Uri uri) {
    try {
      // Use the MediaStore to load the imageName.
      Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
      if (image != null) {
        this.image = image;
      }

    } catch (IOException e) {
      e.printStackTrace();
      Log.e(LOG_TAG, "Error: " + e.getMessage() + "Could not open URI: " + uri.toString());
    }
  }

  private String getFileName(@NonNull Uri uri) {
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

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }


  // - - - - - - - - - - - - - - - Load Recipe AsyncTask - - - - - - - - - - - - - - -

  private class LoadRecipeAsyncTask extends AsyncTask<Integer, Void, RecipeHolder> {

    // Member variables
    private ProgressBar progressBar;
    private NestedScrollView contentScrollView;
    private EditText titleInput;
    private EditText servingsInput;
    private EditText descriptionInput;
    private TextView imageNameView;
    private AddIngredientListAdapter addIngredientListAdapter;
    private AddStepListAdapter addStepListAdapter;


    // Constructor
    LoadRecipeAsyncTask(ProgressBar progressBar,
                        NestedScrollView contentScrollView,
                        EditText titleInput,
                        EditText servingsInput,
                        EditText descriptionInput,
                        TextView imageNameView,
                        AddIngredientListAdapter addIngredientListAdapter,
                        AddStepListAdapter addStepListAdapter) {
      this.progressBar = progressBar;
      this.contentScrollView = contentScrollView;
      this.titleInput = titleInput;
      this.servingsInput = servingsInput;
      this.descriptionInput = descriptionInput;
      this.imageNameView = imageNameView;
      this.addIngredientListAdapter = addIngredientListAdapter;
      this.addStepListAdapter = addStepListAdapter;
    }


    // AsyncTask methods
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      contentScrollView.setVisibility(View.GONE);
      progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected RecipeHolder doInBackground(Integer... integers) {
      int recipeId = integers[0];

      return detailViewModel.getRecipeHolder(recipeId);
    }

    @Override
    protected void onPostExecute(RecipeHolder recipeHolder) {
      super.onPostExecute(recipeHolder);

      // Update Views
      titleInput.setText(recipeHolder.getTitle());
      servingsInput.setText(Integer.toString(recipeHolder.getServings()));
      descriptionInput.setText(recipeHolder.getDescription());
      imageNameView.setText(recipeHolder.getImageDirectory());
      addIngredientListAdapter.setData(recipeHolder.getIngredientHolders());
      addStepListAdapter.setData(recipeHolder.getSteps());

      // Update Activity variables
      AddRecipeActivity.this.ingredientHolders = recipeHolder.getIngredientHolders();
      AddRecipeActivity.this.stepHolders = recipeHolder.getSteps();

      // Update UI
      progressBar.setVisibility(View.GONE);
      contentScrollView.setVisibility(View.VISIBLE);
    }
  }
}
