package com.gerardbradshaw.tomatoes.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.entities.RecipeStep;

@Dao
public interface RecipeStepDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipeStep(RecipeStep recipeStep);

  @Update
  void updateRecipeStep(RecipeStep recipeStep);

  @Delete
  void deleteRecipeStep(RecipeStep recipeStep);

  @Query("select * from recipe_step_table where recipe_id = :recipeId")
  RecipeStep[] getRecipeSteps(int recipeId);

}
