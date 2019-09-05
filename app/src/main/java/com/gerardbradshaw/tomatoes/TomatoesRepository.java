package com.gerardbradshaw.tomatoes;

import android.app.Application;
import android.util.Pair;
import com.gerardbradshaw.tomatoes.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeDao;
import com.gerardbradshaw.tomatoes.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeStepDao;
import com.gerardbradshaw.tomatoes.entities.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TomatoesRepository {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private IngredientDao ingredientDao;
  private RecipeDao recipeDao;
  private RecipeIngredientDao recipeIngredientDao;
  private RecipeStepDao recipeStepDao;

  private ArrayList<Recipe> recipeList;

  //TODO add live data


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  TomatoesRepository(Application application) {

    // Initialize the database and get a handle on the DAOs
    TomatoesRoomDatabase db = TomatoesRoomDatabase.getDatabase(application);
    ingredientDao = db.ingredientDao();
    recipeDao = db.recipeDao();
    recipeIngredientDao = db.recipeIngredientDao();
    recipeStepDao = db.recipeStepDao();

    // Get the list of recipes and cache them in recipeList
    recipeList = new ArrayList<> (Arrays.asList(recipeDao.getAllRecipes()));
  }


  // - - - - - - - - - - - - - - - Wrapper methods - - - - - - - - - - - - - - -

  public ArrayList<Recipe> getAllRecipes() {
    return recipeList;
  }


}
