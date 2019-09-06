package com.gerardbradshaw.tomatoes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeSummaryDao;
import com.gerardbradshaw.tomatoes.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeStepDao;
import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.List;

public class RecipeRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientDao ingredientDao;
  private RecipeSummaryDao recipeSummaryDao;
  private RecipeIngredientDao recipeIngredientDao;
  private RecipeStepDao recipeStepDao;

  private LiveData<List<RecipeSummary>> recipeSummaryList;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * Initializes the database
   *
   * @param application: Application context wrapper
   */
  RecipeRepository(Application application) {

    // Initialize the database and get a handle on the DAOs
    TomatoesRoomDatabase db = TomatoesRoomDatabase.getDatabase(application);
    ingredientDao = db.ingredientDao();
    recipeSummaryDao = db.recipeDao();
    recipeIngredientDao = db.recipeIngredientDao();
    recipeStepDao = db.recipeStepDao();

    // Get the list of recipes and cache them in recipeSummaryList
    recipeSummaryList = recipeSummaryDao.getAllRecipes();
  }


  // - - - - - - - - - - - - - - - Wrapper methods - - - - - - - - - - - - - - -

  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  /**
   * Wrapper method for the InsertRecipeAsyncTask class, which calls the insertRecipeSummary method
   * for the RecipeSummaryDao.
   *
   * @param recipeSummary the RecipeSummary to be inserted.
   */
  public void insertRecipeSummary(RecipeSummary recipeSummary) {
    new insertRecipeSummaryAsync(recipeSummaryDao).execute(recipeSummary);
  }

  /**
   * Wrapper method for the DeleteRecipeAsyncTask class, which calls the deleteRecipeSummary method
   * for the RecipeSummaryDao.
   *
   * @param recipeSummary the RecipeSummary to be deleted.
   */
  public void deleteRecipeSummary(RecipeSummary recipeSummary) {
    new deleteRecipeSummaryAsync(recipeSummaryDao).execute(recipeSummary);
  }

  /**
   * Wrapper method for the updateRecipeAsyncTask class, which calls the updateRecipeSummary method
   * for the RecipeSummaryDao.
   *
   * @param recipeSummary the RecipeSummary to be updated.
   */
  public void updateRecipeSummary(RecipeSummary recipeSummary) {
    new updateRecipeSummaryAsync(recipeSummaryDao).execute(recipeSummary);
  }


  // - - - - - - - - - - - - - - - AsyncTasks for wrapper methods - - - - - - - - - - - - - - -

  /**
   * AsyncTask for insertRecipeSummary.
   */
  private static class insertRecipeSummaryAsync extends AsyncTask<RecipeSummary, Void, Void> {

    // Member variable for the the DAO
    private RecipeSummaryDao dao;

    // Constructor
    insertRecipeSummaryAsync(RecipeSummaryDao dao) {
      this.dao = dao;
    }

    // Background task
    @Override
    protected Void doInBackground(final RecipeSummary... recipeSummaries) {
      dao.insertRecipe(recipeSummaries[0]);
      return null;
    }
  }

  /**
   * AsyncTask for deleteRecipeSummary.
   */
  private static class deleteRecipeSummaryAsync extends AsyncTask<RecipeSummary, Void, Void> {

    // Member variable
    private RecipeSummaryDao dao;

    // Constructor
    deleteRecipeSummaryAsync(RecipeSummaryDao dao) {
      this.dao = dao;
    }

    // Background task
    @Override
    protected Void doInBackground(RecipeSummary... recipeSummaries) {
      dao.deleteRecipe(recipeSummaries[0]);
      return null;
    }
  }

  /**
   * AsyncTask for updateRecipeSummary.
   */
  private static class updateRecipeSummaryAsync extends AsyncTask<RecipeSummary, Void, Void> {

    // Member variable for the dao
    private RecipeSummaryDao dao;

    // Constructor
    updateRecipeSummaryAsync(RecipeSummaryDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(RecipeSummary... recipeSummaries) {
      dao.updateRecipe(recipeSummaries[0]);
      return null;
    }
  }

}
