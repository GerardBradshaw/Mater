package com.gerardbradshaw.tomatoes.room;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gerardbradshaw.tomatoes.helpers.AsyncTaskScheduler;
import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.tomatoes.room.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.SummaryDao;
import com.gerardbradshaw.tomatoes.room.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.StepDao;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientDao ingredientDao;
  private SummaryDao summaryDao;
  private RecipeIngredientDao recipeIngredientDao;
  private StepDao stepDao;

  private LiveData<List<RecipeSummary>> recipeSummaryList;
  private static MutableLiveData<Integer> liveImageChanger = new MutableLiveData<>();
  private static AtomicInteger updateCount = new AtomicInteger(0);

  private static final String LOG_TAG = "GGG - Repository";
  private static final String PATH = "";
  private static File storage;

  private Context context;

  private AsyncTaskScheduler taskScheduler = new AsyncTaskScheduler();


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * Initializes the database
   *
   * @param application: Application context wrapper
   */
  public RecipeRepository(Application application) {

    // Initialize the database and get a handle on the DAOs
    TomatoesRoomDatabase db = TomatoesRoomDatabase.getDatabase(application);
    ingredientDao = db.ingredientDao();
    summaryDao = db.recipeDao();
    recipeIngredientDao = db.recipeIngredientDao();
    stepDao = db.recipeStepDao();

    // Downcast the application and set the context
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    context = tomatoesApplication.getApplicationContext();

    // Cache LiveData
    recipeSummaryList = summaryDao.getAllSummaries();

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


  // - - - - - - - - - - - - - - - LiveData Getters - - - - - - - - - - - - - - -

  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  public LiveData<String> getRecipeTitle(int recipeId) {
    return summaryDao.getTitle(recipeId);
  }

  public LiveData<String> getRecipeDescription(int recipeId) {
    return summaryDao.getDescription(recipeId);
  }

  public LiveData<String> getRecipeImageDirectory(int recipeId) {
    return summaryDao.getImageDirectory(recipeId);
  }

  public LiveData<RecipeIngredient[]> getRecipeIngredients(int recipeId) {
    return recipeIngredientDao.getRecipeIngredients(recipeId);
  }

  public LiveData<RecipeStep[]> getRecipeSteps(int recipeId) {
    return stepDao.getAllSteps(recipeId);
  }

  public LiveData<Integer> getRecipeId(String recipeTitle) {
    return summaryDao.getRecipeIdLiveData(recipeTitle);
  }


  // - - - - - - - - - - - - - - - Load Images - - - - - - - - - - - - - - -

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


  // - - - - - - - - - - - - - - - Save Images - - - - - - - - - - - - - - -

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


  // - - - - - - - - - - - - - - - Insert Recipe from Holder - - - - - - - - - - - - - - -

  /**
   * Inserts a recipe to the database from a RecipeHolder.
   *
   * @param recipeHolder: the recipe to be inserted.
   */
  public void insertRecipeFromHolder(final RecipeHolder recipeHolder) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new InsertRecipeFromHolderAsyncTask(
            summaryDao, recipeIngredientDao, stepDao, ingredientDao)
            .execute(recipeHolder);
      }
    };

    taskScheduler.addNewTask(runnable);
  }

  private class InsertRecipeFromHolderAsyncTask
      extends AsyncTask<RecipeHolder, Void, Void> {

    // Member variables
    private SummaryDao summaryDao;
    private RecipeIngredientDao recipeIngredientDao;
    private StepDao stepDao;
    private IngredientDao ingredientDao;

    // Constructor
    InsertRecipeFromHolderAsyncTask(
        SummaryDao summaryDao, RecipeIngredientDao recipeIngredientDao,
        StepDao stepDao, IngredientDao ingredientDao) {

      this.summaryDao = summaryDao;
      this.recipeIngredientDao = recipeIngredientDao;
      this.stepDao = stepDao;
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Void doInBackground(RecipeHolder... recipeHolders) {
      // Get the RecipeHolder object
      RecipeHolder recipe = recipeHolders[0];

      // Add the info to the database
      addSummaryToDb(recipe.getTitle(), recipe.getDescription(), recipe.getImageDirectory());

      // Get the ID of the new recipe
      int recipeId = summaryDao.getRecipeId(recipe.getTitle());

      // Add the steps to the database
      addStepsToDb(recipeId, recipe.getSteps());

      // Add the ingredients to database
      addIngredientsToDb(recipeId, recipe.getRecipeIngredients());

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      taskScheduler.setTaskFinished();
    }

    private void addSummaryToDb(String title, String description, String imageDirectory) {

      // Change the input if anything is null
      if(title == null) {
        title = "Not set";
      }
      if(description == null) {
        description = "Not set";
      }
      if(imageDirectory == null) {
        imageDirectory = "";
      }

      // Create a RecipeSummary from the input
      RecipeSummary recipeSummary = new RecipeSummary(title, description, imageDirectory);

      // Add the title and description to the database.
      summaryDao.insertSummary(recipeSummary);

    }

    private void addStepsToDb(int recipeId, List<String> steps) {

      for(int i = 0; i < steps.size(); i++) {

        // Define the step number
        int stepNumber = i + 1;

        // Create a RecipeStep to add to the DAO
        RecipeStep recipeStep = new RecipeStep(recipeId, stepNumber, steps.get(i));

        // Add the RecipeStep to the DAO
        stepDao.insertStep(recipeStep);
      }
    }

    private void addIngredientsToDb(int recipeId, List<RecipeIngredientHolder> ingredients) {

      for(int i = 0; i < ingredients.size(); i++) {

        // Get the name, amount, and units of the ingredient from the array
        String name = ingredients.get(i).getName();
        double amount = ingredients.get(i).getAmount();
        String units = ingredients.get(i).getUnit();

        // Get the ID of the ingredient from the DB. If it does not exist yet, the ID = 0.
        int ingredientId = ingredientDao.getIngredientId(name);

        // If the ingredient does not exist in the ingredient_table, then add it
        if (ingredientId == 0) {
          // Create an ingredient from the name
          Ingredient ingredient = new Ingredient(name);

          // Add the Ingredient to the DAO
          ingredientDao.insertIngredient(ingredient);

          // Get the ID of the ingredient from the DAO
          ingredientId = ingredientDao.getIngredientId(name);
        }

        // Create a RecipeIngredient using this ID along with the RecipeSummary ID, amount, and units
        RecipeIngredient recipeIngredient =
            new RecipeIngredient(recipeId, ingredientId, amount, units);

        // Add the RecipeIngredient to the DAO
        recipeIngredientDao.insertRecipeIngredient(recipeIngredient);

      }
    }


  }


  // - - - - - - - - - - - - - - - Delete Recipe - - - - - - - - - - - - - - -

  public void deleteRecipe(final int recipeId) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        new DeleteRecipeAsyncTask(
            summaryDao, recipeIngredientDao, stepDao, ingredientDao)
            .execute(recipeId);
      }
    };
  }

  private class DeleteRecipeAsyncTask extends AsyncTask<Integer, Void, Void> {

    // Member variables
    private SummaryDao summaryDao;
    private RecipeIngredientDao recipeIngredientDao;
    private StepDao stepDao;
    private IngredientDao ingredientDao;
    private int recipeId;

    // Constructor
    DeleteRecipeAsyncTask(
        SummaryDao summaryDao, RecipeIngredientDao recipeIngredientDao,
        StepDao stepDao, IngredientDao ingredientDao) {

      this.summaryDao = summaryDao;
      this.recipeIngredientDao = recipeIngredientDao;
      this.stepDao = stepDao;
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
      // Get the ID of the recipe to delete
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
      summaryDao;
      recipeIngredientDao;
      stepDao;
      ingredientDao;
    }
  }


  // - - - - - - - - - - - - - - - Update Recipe - - - - - - - - - - - - - - -

  /**
   * Wrapper method for the updateRecipeAsyncTask class, which calls the updateRecipeSummary method
   * for the SummaryDao.
   *
   * @param recipeSummary the RecipeSummary to be updated.
   */
  public void updateRecipeSummary(RecipeSummary recipeSummary) {
    new updateRecipeSummaryAsyncTask(summaryDao).execute(recipeSummary);
  }

  private static class updateRecipeSummaryAsyncTask
      extends AsyncTask<RecipeSummary, Void, Void> {

    // Member variable for the dao
    private SummaryDao dao;

    // Constructor
    updateRecipeSummaryAsyncTask(SummaryDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(RecipeSummary... recipeSummaries) {
      dao.updateSummary(recipeSummaries[0]);
      return null;
    }
  }

  // TODO add all update methods


  // - - - - - - - - - - - - - - - Get Ingredient - - - - - - - - - - - - - - -

  public Ingredient getIngredient(int ingredientId) {
    try {
      return new getIngredientAsyncTask(ingredientDao).execute(ingredientId).get();
    } catch (Exception e) {
      // TODO handle exception
      return null;
    }

  }

  private static class getIngredientAsyncTask extends AsyncTask<Integer, Void, Ingredient> {

    // Member variables
    private IngredientDao ingredientDao;

    // Constructor
    getIngredientAsyncTask(IngredientDao ingredientDao) {
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Ingredient doInBackground(Integer... recipeIds) {
      return ingredientDao.getIngredientFromId(recipeIds[0]);
    }
  }

}
