package com.gerardbradshaw.tomatoes.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;

@Dao
public interface RecipeIngredientDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipeIngredient(RecipeIngredient recipeIngredient);

  @Update
  void updateRecipeIngredient(RecipeIngredient recipeIngredient);

  @Delete
  void deleteRecipeIngredient(RecipeIngredient recipeIngredient);

  @Query("select * from recipe_ingredient_table where recipe_id = :recipeId")
  RecipeIngredient[] getRecipeIngredients(int recipeId);
}
