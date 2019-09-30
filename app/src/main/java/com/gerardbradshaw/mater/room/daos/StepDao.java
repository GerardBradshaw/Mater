package com.gerardbradshaw.mater.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.mater.room.entities.Step;

import java.util.List;

@Dao
public interface StepDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertStep(Step step);

  @Update
  void updateStep(Step step);

  @Delete
  void deleteStep(Step... steps);


  // - - - - - - - - - - - - - - - LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from step_table where recipe_id = :recipeId")
  LiveData<Step[]> getLiveSteps(int recipeId);


  // - - - - - - - - - - - - - - - Non-LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from step_table where recipe_id = :recipeId")
  List<Step> getSteps(int recipeId);

}
