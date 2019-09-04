package com.gerardbradshaw.tomatoes.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.entities.Recipe;

@Dao
public interface RecipeDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipe(Recipe recipe);

  @Delete
  void deleteRecipe(Recipe recipe);

  @Update
  void updateRecipe(Recipe recipe);

  @Query("select description from Recipe where title = :recipeTitle")
  String getDescription(String recipeTitle);

  @Query("select * from Recipe ORDER BY title ASC")
  Recipe[] getAllRecipes();

  @Query("select * from Recipe LIMIT 1")
  Recipe[] getAnyRecipe();
}
