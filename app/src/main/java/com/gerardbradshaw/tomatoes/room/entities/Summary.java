package com.gerardbradshaw.tomatoes.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_summary_table")
public class Summary {


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

  @NonNull
  @ColumnInfo(name = "image_directory")
  private String imageDirectory;


  // - - - - - - - - - - - - - - - Constructors - - - - - - - - - - - - - - -

  // Default constructor
  public Summary(@NonNull String title,
                 @NonNull String description,
                 @NonNull String imageDirectory) {
    this.title = title;
    this.description = description;
    this.imageDirectory = imageDirectory;
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

  @NonNull
  public String getImageDirectory() {
    return imageDirectory;
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

  public void setImageDirectory(@NonNull String imageDirectory) {
    this.imageDirectory = imageDirectory;
  }

}
