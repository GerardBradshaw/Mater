package com.gerardbradshaw.tomatoes.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_step_table",
    foreignKeys = {
    @ForeignKey(entity = RecipeSummary.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id")},

    indices = {@Index(value = "recipe_id")}

)
public class RecipeStep {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "step_id")
  private int stepId;

  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @ColumnInfo(name = "number")
  private int number;

  @NonNull
  @ColumnInfo(name = "description")
  private String description;


  // - - - - - - - - - - - - - - - Constructor(s) - - - - - - - - - - - - - - -

  public RecipeStep(int recipeId, int number, @NonNull String description) {
    this.recipeId = recipeId;
    this.number = number;
    this.description = description;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getStepId() {
    return stepId;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public int getNumber() {
    return number;
  }

  @NonNull
  public String getDescription() {
    return description;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setStepId(int stepId) {
    this.stepId = stepId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setDescription(@NonNull String description) {
    this.description = description;
  }

}
