package com.gerardbradshaw.tomatoes.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.room.entities.Step;

@Dao
public interface StepDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertStep(Step step);

  @Update
  void updateStep(Step step);

  @Delete
  void deleteStep(Step step);

  @Query("select * from recipe_step_table where recipe_id = :recipeId")
  LiveData<Step[]> getAllSteps(int recipeId);

  @Query("delete from recipe_step_table where recipe_id = :recipeId")
  void deleteSteps(int recipeId);

}
