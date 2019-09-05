package com.gerardbradshaw.tomatoes.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "recipe_summary_table")
public class RecipeSummary {


  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @NonNull
  @ColumnInfo(name = "description")
  private String description;


  // - - - - - - - - - - - - - - - Constructors - - - - - - - - - - - - - - -

  // Default constructor
  public RecipeSummary(@NonNull String title, @NonNull String description) {
    this.title = title;
    this.description = description;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getRecipeId() {
    return recipeId;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  @NonNull
  public String getDescription() {
    return description;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setDescription(@NonNull String description) {
    this.description = description;
  }

}
