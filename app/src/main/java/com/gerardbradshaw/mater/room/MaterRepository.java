package com.gerardbradshaw.mater.room;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gerardbradshaw.mater.util.AsyncTaskScheduler;
import com.gerardbradshaw.mater.util.MaterApplication;
import com.gerardbradshaw.mater.models.IngredientHolder;
import com.gerardbradshaw.mater.models.RecipeHolder;
import com.gerardbradshaw.mater.room.daos.IngredientDao;
import com.gerardbradshaw.mater.room.daos.SummaryDao;
import com.gerardbradshaw.mater.room.daos.StepDao;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Step;
import com.gerardbradshaw.mater.room.entities.Summary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MaterRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private SummaryDao summaryDao;
  private IngredientDao ingredientDao;
  private StepDao stepDao;

  private LiveData<List<Summary>> liveRecipeSummaryList;

  private static MutableLiveData<Integer> liveImageChanger = new MutableLiveData<>();
  private static AtomicInteger updateCount = new AtomicInteger(0);

  private static final String LOG_TAG = "GGG - Repository";
  private static final String PATH = "";

  private static File storage;

  private Context context;
  private AsyncTaskScheduler taskScheduler;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public MaterRepository(Application application) {

    // Initialize the database and get a handle on the DAOs
    MaterRoomDatabase db = MaterRoomDatabase.getDatabase(application);
    summaryDao = db.recipeDao();
    ingredientDao = db.ingredientDao();
    stepDao = db.recipeStepDao();

    // Downcast the application and set the context
    MaterApplication materApplication = (MaterApplication) application;
    context = materApplication.getApplicationContext();
    taskScheduler = materApplication.getTaskScheduler();

    // Cache LiveData
    liveRecipeSummaryList = summaryDao.getLiveAllSummaries();

    // Determine the path to internal storage and create a File object
    File internalStorage = application.getFilesDir();
    storage = new File(internalStorage, PATH);

    // Create the directory for images
    if (!storage.exists()) {
      if (!storage.mkdirs()) {
        Log.d(LOG_TAG, "Could not create storage directory: " + storage.getAbsolutePath());
      }
    }

  }


  // - - - - - - - - - - - - - - - Summary Data - - - - - - - - - - - - - - -

  public LiveData<List<Summary>> getLiveAllSummaries() {
    return liveRecipeSummaryList;
  }

  public LiveData<String> getLiveTitle(int recipeId) {
    return summaryDao.getLiveTitle(recipeId);
  }

  public LiveData<String> getLiveDescription(int recipeId) {
    return summaryDao.getLiveDescription(recipeId);
  }

  public LiveData<Integer> getLiveServings(int recipeId) {
    return summaryDao.getLiveServings(recipeId);
  }

  public LiveData<String> getLiveImageDirectory(int recipeId) {
    return summaryDao.getLiveImageDirectory(recipeId);
  }

  public LiveData<Integer> getLiveRecipeId(String recipeTitle) {
    return summaryDao.getLiveRecipeId(recipeTitle);
  }

  public List<Summary> getAllSummaries() {
    return summaryDao.getAllSummaries();
  }


  // - - - - - - - - - - - - - - - Ingredient Data - - - - - - - - - - - - - - -

  public LiveData<List<Ingredient>> getLiveIngredients(int recipeId) {
    return ingredientDao.getLiveIngredients(recipeId);
  }

  public List<Ingredient> getIngredients(int recipeId) {
    return ingredientDao.getIngredients(recipeId);
  }


  // - - - - - - - - - - - - - - - Step Data - - - - - - - - - - - - - - -

  public LiveData<Step[]> getLiveSteps(int recipeId) {
    return stepDao.getLiveSteps(recipeId);
  }

  public List<Step> getSteps(int recipeId) {
    return stepDao.getSteps(recipeId);
  }


  // - - - - - - - - - - - - - - - Loading Images - - - - - - - - - - - - - - -

  public Bitmap loadBitmap(Context context, String recipeTitle) {
    final String filename = recipeTitle + ".jpg";

    // Load the file from storage
    try (FileInputStream fileInputStream = context.openFileInput(filename)) {
      return BitmapFactory.decodeStream(fileInputStream);

    } catch (IOException e) {
      Log.e(LOG_TAG, "Error during load of image: " + e.getMessage());
      return null;
    }

  }

  public File getFile(String recipeTitle) {
    return new File(storage, recipeTitle + ".jpg");
  }

  public LiveData<Integer> bitmapUpdateNotifier() {
    return liveImageChanger;
  }


  // - - - - - - - - - - - - - - - Saving Images - - - - - - - - - - - - - - -

  public void storeBitmap(final String recipeTitle, final Bitmap image) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new StoreBitmapAsyncTask(recipeTitle, image).execute();
      }
    };

    taskScheduler.addNewTask(runnable);
  }

  public void storeBitmap(final String recipeTitle, final int resourceId) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new StoreBitmapAsyncTask(recipeTitle, resourceId).execute();
      }
    };

    taskScheduler.addNewTask(runnable);
  }

  private class StoreBitmapAsyncTask extends AsyncTask<Void, Void, Boolean> {

    // Member variables
    private String fileName;
    private Boolean success;
    private int resourceId;
    private Bitmap image;

    // Constructors
    StoreBitmapAsyncTask(String recipeTitle, Bitmap image) {
      fileName = recipeTitle + ".jpg";
      this.image = image;
      resourceId = 0;
    }

    StoreBitmapAsyncTask(String recipeTitle, int resourceId) {
      fileName = recipeTitle + ".jpg";
      this.resourceId = resourceId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
      if (resourceId != 0) {
        image = BitmapFactory.decodeResource(context.getResources(), resourceId);
      }

      // Get the bitmap and create a file
      File file = new File(storage, fileName);

      // Save the file to storage. File is overwritten if one already exists for the recipe.
      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
        Log.d(LOG_TAG, "Saving " + fileName);
        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        Log.d(LOG_TAG, "Saved " + fileName);
        success = true;

      } catch (IOException e) {
        Log.e(LOG_TAG, "Error while saving " + fileName + ": " + e.getMessage());
        success = false;
      }
      return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
      super.onPostExecute(success);

      taskScheduler.setTaskFinished();

      // If there are any listeners, this will update them
      liveImageChanger.setValue(updateCount.getAndIncrement());
      Log.d(LOG_TAG, "Counter is now "+ liveImageChanger.getValue());
    }
  }


  // - - - - - - - - - - - - - - - Loading Ingredients - - - - - - - - - - - - - - -

  public List<Ingredient> getAllIngredients() {
    return ingredientDao.getAllIngredients();
  }

  public Ingredient getIngredient(final int ingredientId) {
    return ingredientDao.getIngredient(ingredientId);
  }


  // - - - - - - - - - - - - - - - Saving New Ingredients - - - - - - - - - - - - - - -

  public void addIngredient(final Ingredient... ingredients) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new InsertIngredientAsyncTask(ingredientDao).execute(ingredients);
      }
    };
    taskScheduler.addNewTask(runnable);
  }

  public void addIngredient(final List<Ingredient> ingredients) {
    Ingredient[] ingredientsArray = new Ingredient[ingredients.size()];
    ingredientsArray = ingredients.toArray(ingredientsArray);
    addIngredient(ingredientsArray);
  }

  private class InsertIngredientAsyncTask extends AsyncTask<Ingredient, Void, Void> {

    private IngredientDao ingredientDao;

    InsertIngredientAsyncTask(IngredientDao ingredientDao) {
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Void doInBackground(Ingredient... ingredients) {
      for (Ingredient i : ingredients) {
        ingredientDao.insertIngredient(i);
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();
    }
  }


  // - - - - - - - - - - - - - - - Updating Ingredients - - - - - - - - - - - - - - -

  public void updateIngredientOnCurrentThread(List<Ingredient> ingredients) {
    for (Ingredient i : ingredients) {
      ingredientDao.updateIngredient(i);
    }
  }

  public void updateIngredients(final Ingredient... ingredients) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new UpdateIngredientAsyncTask(ingredientDao).execute(ingredients);
      }
    };
    taskScheduler.addNewTask(runnable);
  }

  public void updateIngredients(final List<Ingredient> ingredients) {
    Ingredient[] ingredientArray = new Ingredient[ingredients.size()];
    ingredientArray = ingredients.toArray(ingredientArray);
    updateIngredients(ingredientArray);
  }

  private class UpdateIngredientAsyncTask extends AsyncTask<Ingredient, Void, Void> {

    private IngredientDao ingredientDao;

    UpdateIngredientAsyncTask(IngredientDao ingredientDao) {
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Void doInBackground(Ingredient... ingredients) {
      for (Ingredient i : ingredients) {
        ingredientDao.updateIngredient(i);
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();
    }
  }


  // - - - - - - - - - - - - - - - Insert Recipe - - - - - - - - - - - - - - -

  /**
   * Inserts a recipe to the database from a RecipeHolder.
   *
   * @param recipeHolder: the recipe to be inserted.
   */
  public void insertRecipeFromHolder(final RecipeHolder recipeHolder) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new InsertRecipeFromHolderAsyncTask(summaryDao, ingredientDao, stepDao).execute(recipeHolder);
      }
    };
    taskScheduler.addNewTask(runnable);
  }

  private class InsertRecipeFromHolderAsyncTask extends AsyncTask<RecipeHolder, Void, Void> {

    // Member variables
    private SummaryDao summaryDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;


    // Constructor
    InsertRecipeFromHolderAsyncTask(
        SummaryDao summaryDao, IngredientDao ingredientDao, StepDao stepDao) {

      this.summaryDao = summaryDao;
      this.ingredientDao = ingredientDao;
      this.stepDao = stepDao;
    }

    @Override
    protected Void doInBackground(RecipeHolder... recipeHolders) {
      // Get the RecipeHolder object
      RecipeHolder recipe = recipeHolders[0];

      // Add the info to the database
      addSummaryToDb(
          recipe.getTitle(),
          recipe.getDescription(),
          recipe.getImageDirectory(),
          recipe.getServings());

      // Get the ID of the new recipe
      int recipeId = summaryDao.getRecipeId(recipe.getTitle());

      // Add the steps to the database
      addStepsToDb(recipeId, recipe.getSteps());

      // Add the ingredients to database
      addIngredientsToDb(recipeId, recipe.getIngredientHolders());

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      taskScheduler.setTaskFinished();
    }

    private void addSummaryToDb(String title, String description, String imageDirectory, int servings) {

      // Change the input if anything is null
      if (title == null) {
        title = "Not set";
      }
      if (description == null) {
        description = "Not set";
      }
      if (imageDirectory == null) {
        imageDirectory = "";
      }
      if (servings == 0) {
        servings = 1;
      }

      // Create a Summary from the input
      Summary summary = new Summary(title, description, imageDirectory, servings);

      // Determine if the recipe has already been added before (ID = 0)
      int recipeId = summaryDao.getRecipeId(title);

      if (recipeId != 0) {
        summary.setRecipeId(recipeId);
      }

      // Add summary to database
      summaryDao.insertSummary(summary);

    }

    private void addStepsToDb(int recipeId, List<String> steps) {

      for(int i = 0; i < steps.size(); i++) {
        // Define the step number
        int stepNumber = i + 1;

        // Create a Step to add to the DAO
        Step step = new Step(recipeId, stepNumber, steps.get(i));

        // Add the Step to the DAO
        stepDao.insertStep(step);
      }
    }

    private void addIngredientsToDb(int recipeId, List<IngredientHolder> ingredients) {

      for(int i = 0; i < ingredients.size(); i++) {
        // Get the name, amount, and units of the ingredient from the array
        String name = ingredients.get(i).getName();
        double amount = ingredients.get(i).getAmount();
        String units = ingredients.get(i).getUnit();
        String category = ingredients.get(i).getCategory();

        // Create a Ingredient and add it to the database
        Ingredient ingredient = new Ingredient(name, category, recipeId, amount, units);
        ingredientDao.insertIngredient(ingredient);
      }
    }
  }


  // - - - - - - - - - - - - - - - Get Recipe - - - - - - - - - - - - - - -

  /**
   * Returns a RecipeHolder containing all information for the requested RecipeID. Do not call on UI
   * thread.
   * @param recipeId, int
   * @return RecipeHolder
   */
  public RecipeHolder getRecipeHolder(int recipeId) {

    RecipeHolder holder = new RecipeHolder();

    // Set the summary information
    Summary summary = summaryDao.getSummary(recipeId);
    holder.setTitle(summary.getTitle());
    holder.setDescription(summary.getDescription());
    holder.setImageDirectory(summary.getImageDirectory());
    holder.setServings(summary.getServings());

    // Set the step information
    List<Step> steps = stepDao.getSteps(recipeId);
    List<String> stepStrings = new ArrayList<>();
    for (Step step : steps) {
      stepStrings.add(step.getDescription());
    }
    holder.setSteps(stepStrings);

    // Set the ingredient information
    List<Ingredient> ingredients = ingredientDao.getIngredients(recipeId);
    List<IngredientHolder> ingredientHolders = new ArrayList<>();

    for (Ingredient ingredient : ingredients) {

      IngredientHolder ingredientHolder = new IngredientHolder(
          ingredient.getName(),
          ingredient.getCategory(),
          ingredient.getAmount(),
          ingredient.getUnits());

      ingredientHolder.setInStock(ingredient.getInStock());
      ingredientHolders.add(ingredientHolder);
    }
    holder.setIngredientHolders(ingredientHolders);

    return holder;
  }


  // - - - - - - - - - - - - - - - Delete Recipe - - - - - - - - - - - - - - -

  public void deleteRecipe(final int recipeId) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new DeleteRecipeAsyncTask(summaryDao, ingredientDao, stepDao).execute(recipeId);
      }
    };
    taskScheduler.addNewTask(runnable);
  }

  private class DeleteRecipeAsyncTask extends AsyncTask<Integer, Void, Void> {

    // Member variables
    private SummaryDao summaryDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;
    private int recipeId;

    // Constructor
    DeleteRecipeAsyncTask(
        SummaryDao summaryDao, IngredientDao ingredientDao, StepDao stepDao) {

      this.summaryDao = summaryDao;
      this.ingredientDao = ingredientDao;
      this.stepDao = stepDao;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
      recipeId = integers[0];
      deleteRecipe();
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      taskScheduler.setTaskFinished();
    }

    private void deleteRecipe() {

      Summary summary = summaryDao.getSummary(recipeId);
      summaryDao.deleteSummary(summary);

      // TODO delete steps and ingredients
      //Step[] steps = stepDao.getSteps(recipeId);
      //stepDao.deleteStep(steps);
    }
  }


  // - - - - - - - - - - - - - - - Update Recipe - - - - - - - - - - - - - - -

  /**
   * Wrapper method for the updateRecipeAsyncTask class, which calls the updateRecipeSummary method
   * for the SummaryDao.
   *
   * @param summary the Summary to be updated.
   */
  public void updateRecipeSummary(Summary summary) {
    new updateRecipeSummaryAsyncTask(summaryDao).execute(summary);
  }

  private static class updateRecipeSummaryAsyncTask
      extends AsyncTask<Summary, Void, Void> {

    // Member variable for the dao
    private SummaryDao dao;

    // Constructor
    updateRecipeSummaryAsyncTask(SummaryDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Summary... recipeSummaries) {
      dao.updateSummary(recipeSummaries[0]);
      return null;
    }
  }
}
