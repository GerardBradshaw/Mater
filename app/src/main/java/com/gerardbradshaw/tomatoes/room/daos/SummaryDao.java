package com.gerardbradshaw.tomatoes.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.room.entities.Summary;

import java.util.List;

@Dao
public interface SummaryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSummary(Summary summary);

  @Delete
  void deleteSummary(Summary... summaries);

  @Update
  void updateSummary(Summary summary);


  // - - - - - - - - - - - - - - - LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from summary_table ORDER BY title ASC")
  LiveData<List<Summary>> getLiveAllSummaries();

  @Query("select title from summary_table where recipe_id = :recipeId")
  LiveData<String> getLiveTitle(int recipeId);

  @Query("select description from summary_table where recipe_id = :recipeId")
  LiveData<String> getLiveDescription(int recipeId);

  @Query("select image_directory from summary_table where recipe_id = :recipeId")
  LiveData<String> getLiveImageDirectory(int recipeId);

  @Query("select recipe_id from summary_table where title = :recipeTitle")
  LiveData<Integer> getLiveRecipeId(String recipeTitle);


  // - - - - - - - - - - - - - - - Non-LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from summary_table LIMIT 1")
  Summary[] getAnySummary();

  @Query("select recipe_id from summary_table where title = :recipeTitle")
  int getRecipeId(String recipeTitle);

  @Query("select * from summary_table where recipe_id = :recipeId")
  Summary[] getSummary(int recipeId);
}
