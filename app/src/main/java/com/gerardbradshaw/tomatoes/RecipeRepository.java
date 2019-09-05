package com.gerardbradshaw.tomatoes;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeSummaryDao;
import com.gerardbradshaw.tomatoes.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeStepDao;
import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientDao ingredientDao;
  private RecipeSummaryDao recipeSummaryDao;
  private RecipeIngredientDao recipeIngredientDao;
  private RecipeStepDao recipeStepDao;

  private LiveData<List<RecipeSummary>> recipeSummaryList;

  //TODO add live data


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

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


}
