package com.gerardbradshaw.tomatoes.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;

@Dao
public interface StepDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertStep(RecipeStep recipeStep);

  @Update
  void updateStep(RecipeStep recipeStep);

  @Delete
  void deleteStep(RecipeStep recipeStep);

  @Query("select * from recipe_step_table where recipe_id = :recipeId")
  LiveData<RecipeStep[]> getAllSteps(int recipeId);

  @Query("delete from recipe_step_table where recipe_id = :recipeId")
  void deleteSteps(int recipeId);

}
