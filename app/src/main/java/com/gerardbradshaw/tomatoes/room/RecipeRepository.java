package com.gerardbradshaw.tomatoes.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.tomatoes.room.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.RecipeSummaryDao;
import com.gerardbradshaw.tomatoes.room.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.RecipeStepDao;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class RecipeRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // DAOs
  private IngredientDao ingredientDao;
  private RecipeSummaryDao recipeSummaryDao;
  private RecipeIngredientDao recipeIngredientDao;
  private RecipeStepDao recipeStepDao;

  // LiveData
  private LiveData<List<RecipeSummary>> recipeSummaryList;


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
    recipeSummaryDao = db.recipeDao();
    recipeIngredientDao = db.recipeIngredientDao();
    recipeStepDao = db.recipeStepDao();

    // Cache the LiveData
    recipeSummaryList = recipeSummaryDao.getAllRecipes();
  }


  // - - - - - - - - - - - - - - - Query methods - - - - - - - - - - - - - - -

  /**
   * Gets a live list of recipe summaries from the database
   *
   * @return LiveData List of all RecipeSummary in the database
   */
  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  /**
   * Gets a live String of the title.
   *
   * @param recipeId, int: The ID of the recipe
   * @return LiveData String of the title.
   */
  public LiveData<String> getRecipeTitle(int recipeId) {
    return recipeSummaryDao.getTitle(recipeId);
  }

  /**
   * Gets a live String of the description.
   *
   * @param recipeId, int: The ID of the recipe.
   * @return LiveData String of the description.
   */
  public LiveData<String> getRecipeDescription(int recipeId) {
    return recipeSummaryDao.getDescription(recipeId);
  }

  /**
   * Gets a live list of recipe ingredients for a given recipe ID.
   *
   * @param recipeId, int: The ID of the recipe.
   * @return LiveData List of RecipeIngredients for the given ID.
   */
  public LiveData<RecipeIngredient[]> getRecipeIngredients(int recipeId) {
    return recipeIngredientDao.getRecipeIngredients(recipeId);
  }

  /**
   * Gets a live list of recipe steps for a given recipe ID.
   *
   * @param recipeId, int: The ID of the recipe.
   * @return LiveData List of RecipeSteps for the given ID.
   */
  public LiveData<RecipeStep[]> getRecipeSteps(int recipeId) {
    return recipeStepDao.getRecipeSteps(recipeId);
  }

  public Ingredient getIngredient(int ingredientId) {
    return ingredientDao.getIngredientFromId(ingredientId);
  }


  // - - - - - - - - - - - - - - - Insert, Update and Delete methods - - - - - - - - - - - - - - -

  /**
   * Inserts a recipe to the database from a RecipeHolder.
   *
   * @param recipeHolder: the recipe to be inserted.
   */
  public void insertRecipeFromHolder(RecipeHolder recipeHolder) {
    new insertRecipeFromHolderAsyncTask(
        recipeSummaryDao,
        recipeIngredientDao,
        recipeStepDao,
        ingredientDao)
        .execute(recipeHolder);
  }

  /**
   * Gets a recipe from the database.
   *
   * @param recipeId, int: The ID of the desired recipe.
   * @return RecipeHolder: The requested recipe as a RecipeHolder.
   */
  public RecipeHolder getRecipeHolder(int recipeId) {

    try {
      return new
          getRecipeHolderAsyncTask(
          recipeSummaryDao, recipeIngredientDao, recipeStepDao, ingredientDao)
          .execute(recipeId).get();

    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Wrapper method for the updateRecipeAsyncTask class, which calls the updateRecipeSummary method
   * for the RecipeSummaryDao.
   *
   * @param recipeSummary the RecipeSummary to be updated.
   */
  public void updateRecipeSummary(RecipeSummary recipeSummary) {
    new updateRecipeSummaryAsyncTask(recipeSummaryDao).execute(recipeSummary);
  }

  // TODO add proper update methods


  // - - - - - - - - - - - - - - - AsyncTasks for Insert, Update and Delete - - - - - - - - - - - -

  /**
   * AsyncTask class for insertRecipeFromHolder.
   */
  private static class insertRecipeFromHolderAsyncTask
      extends AsyncTask<RecipeHolder, Void, Void> {

    // Member variables
    private RecipeSummaryDao recipeSummaryDao;
    private RecipeIngredientDao recipeIngredientDao;
    private RecipeStepDao recipeStepDao;
    private IngredientDao ingredientDao;


    // Constructor
    insertRecipeFromHolderAsyncTask(
        RecipeSummaryDao recipeSummaryDao,
        RecipeIngredientDao recipeIngredientDao,
        RecipeStepDao recipeStepDao,
        IngredientDao ingredientDao) {

      this.recipeSummaryDao = recipeSummaryDao;
      this.recipeIngredientDao = recipeIngredientDao;
      this.recipeStepDao = recipeStepDao;
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected Void doInBackground(RecipeHolder... recipeHolders) {

      // Get the RecipeHolder object
      RecipeHolder recipe = recipeHolders[0];

      // Add the Title and Description to the database
      addSummaryToDb(recipe.getTitle(), recipe.getDescription());

      // Get the ID of the new recipe
      int recipeId = recipeSummaryDao.getRecipeId(recipe.getTitle());

      // Add the steps to the database
      addStepsToDb(recipeId, recipe.getSteps());

      // Add the ingredients to database
      addIngredientsToDb(recipeId, recipe.getRecipeIngredients());

      return null;
    }


    /**
     * Simple helper method to add the summary of a recipe to the database.
     *
     * @param title, String: The title of the recipe.
     * @param description, String: The description of the recipe.
     */
    private void addSummaryToDb(String title, String description) {

      // Create a RecipeSummary from the input
      RecipeSummary recipeSummary = new RecipeSummary(title, description);

      // Add the title and description to the database.
      recipeSummaryDao.insertRecipe(recipeSummary);

    }

    /**
     * Simple helper method to add the steps of a recipe to the database.
     *
     * @param recipeId, int: The recipe ID of the corresponding recipe.
     * @param steps, List of Strings: The steps of the recipe in order.
     */
    private void addStepsToDb(int recipeId, List<String> steps) {

      for(int i = 0; i < steps.size(); i++) {

        // Define the step number
        int stepNumber = i + 1;

        // Create a RecipeStep to add to the DAO
        RecipeStep recipeStep = new RecipeStep(recipeId, stepNumber, steps.get(i));

        // Add the RecipeStep to the DAO
        recipeStepDao.insertRecipeStep(recipeStep);
      }
    }

    /**
     * Helper method used to add a list of Ingredients and the amounts used in a specific recipe to
     * the database. If an ingredient already exists as an ingredient, it is not duplicated.
     *
     * @param recipeId, int: The ID of the recipe to which the ingredients belong.
     * @param ingredients, List of RecipeIngredientHolder: The ingredients.
     */
    private void addIngredientsToDb(int recipeId, List<RecipeIngredientHolder> ingredients) {

      for(int i = 0; i < ingredients.size(); i++) {

        // Get the name, amount, and units of the ingredient from the array
        String name = ingredients.get(i).getName();
        double amount = ingredients.get(i).getAmount();
        String units = ingredients.get(i).getUnits();

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

  /**
   * AsyncTask class for getRecipeHolder.
   */
  private static class getRecipeHolderAsyncTask extends AsyncTask<Integer, Void, RecipeHolder> {

    // Member variables
    private int recipeId;
    private RecipeSummaryDao recipeSummaryDao;
    private RecipeIngredientDao recipeIngredientDao;
    private RecipeStepDao recipeStepDao;
    private IngredientDao ingredientDao;

    // Constructor
    getRecipeHolderAsyncTask(
        RecipeSummaryDao recipeSummaryDao,
        RecipeIngredientDao recipeIngredientDao,
        RecipeStepDao recipeStepDao,
        IngredientDao ingredientDao) {

      this.recipeSummaryDao = recipeSummaryDao;
      this.recipeIngredientDao = recipeIngredientDao;
      this.recipeStepDao = recipeStepDao;
      this.ingredientDao = ingredientDao;
    }

    @Override
    protected RecipeHolder doInBackground(Integer... integers) {

      // Get the recipe ID
      int recipeId = integers[0];

      // Create a RecipeHolder for the recipe
      RecipeHolder recipe = new RecipeHolder();

      // Get the recipe information from the DAOs
      //LiveData<RecipeSummary> summary = recipeSummaryDao.getRecipeSummary(recipeId);
      //LiveData<RecipeIngredient> ingredients = recipeIngredientDao.getRecipeIngredients(recipeId);
      //LiveData<RecipeStep> steps = recipeStepDao.getRecipeSteps(recipeId);




      return null;
    }
  }

  /**
   * AsyncTask for updateRecipeSummary.
   */
  private static class updateRecipeSummaryAsyncTask
      extends AsyncTask<RecipeSummary, Void, Void> {

    // Member variable for the dao
    private RecipeSummaryDao dao;

    // Constructor
    updateRecipeSummaryAsyncTask(RecipeSummaryDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(RecipeSummary... recipeSummaries) {
      dao.updateRecipe(recipeSummaries[0]);
      return null;
    }
  }

}
