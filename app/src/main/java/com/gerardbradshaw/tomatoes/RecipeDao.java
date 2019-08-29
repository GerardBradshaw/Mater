package com.gerardbradshaw.tomatoes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public interface RecipeDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipe(Recipe recipe);

  @Delete
  void deleteRecipe(Recipe recipe);

  @Update
  void updateRecipe(Recipe recipe);

  @Query("select description from recipe_table where title = :recipeTitle")
  String getDescription(String recipeTitle);

  @Query("select ingredients from recipe_table where title = :recipeTitle")
  List<String> getIngredients(String recipeTitle);

  @Query("select steps from recipe_table where title = :recipeTitle")
  List<String> getSteps(String recipeTitle);

  @Query("select quantities from recipe_table where title = :recipeTitle")
  Map<Ingredient, Quantity> getQuantities(String recipeTitle);

  @Query("select * from recipe_table ORDER BY title ASC")
  Recipe[] getAllRecipes();

  @Query("select title, description from recipe_table ORDER BY title ASC")
  Recipe[] getAllTitlesAndDescriptions();

  @Query("select * from recipe_table LIMIT 1")
  Recipe[] getAnyRecipe();
}
