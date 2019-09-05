package com.gerardbradshaw.tomatoes.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.List;

@Dao
public interface RecipeSummaryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecipe(RecipeSummary recipeSummary);

  @Delete
  void deleteRecipe(RecipeSummary recipeSummary);

  @Update
  void updateRecipe(RecipeSummary recipeSummary);

  @Query("select description from recipe_summary_table where title = :recipeTitle")
  String getDescription(String recipeTitle);

  @Query("select title from recipe_summary_table where recipe_id = :recipeId")
  String getTitle(int recipeId);

  @Query("select recipe_id from recipe_summary_table where title = :recipeTitle")
  int getRecipeId(String recipeTitle);

  @Query("select * from recipe_summary_table ORDER BY title ASC")
  LiveData<List<RecipeSummary>> getAllRecipes();

  @Query("select * from recipe_summary_table LIMIT 1")
  RecipeSummary[] getAnyRecipe();


}
