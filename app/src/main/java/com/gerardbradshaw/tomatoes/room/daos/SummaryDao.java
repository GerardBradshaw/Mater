package com.gerardbradshaw.tomatoes.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

@Dao
public interface SummaryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSummary(RecipeSummary recipeSummary);

  @Delete
  void deleteSummary(RecipeSummary recipeSummary);

  @Update
  void updateSummary(RecipeSummary recipeSummary);


  // - - - - - - - - - - - - - - - LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from recipe_summary_table ORDER BY title ASC")
  LiveData<List<RecipeSummary>> getAllSummaries();

  @Query("select title from recipe_summary_table where recipe_id = :recipeId")
  LiveData<String> getTitle(int recipeId);

  @Query("select description from recipe_summary_table where recipe_id = :recipeId")
  LiveData<String> getDescription(int recipeId);

  @Query("select image_directory from recipe_summary_table where recipe_id = :recipeId")
  LiveData<String> getImageDirectory(int recipeId);

  @Query("select recipe_id from recipe_summary_table where title = :recipeTitle")
  LiveData<Integer> getRecipeIdLiveData(String recipeTitle);


  // - - - - - - - - - - - - - - - Non-LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from recipe_summary_table LIMIT 1")
  RecipeSummary[] getAnySummary();

  @Query("select recipe_id from recipe_summary_table where title = :recipeTitle")
  int getRecipeId(String recipeTitle);

  @Query("delete from recipe_summary_table where recipe_id = :recipeId")
  void deleteSummary(int recipeId);


}