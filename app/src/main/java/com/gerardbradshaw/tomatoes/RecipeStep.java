package com.gerardbradshaw.tomatoes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_steps_table")
public class RecipeStep {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "step_id")
  private int stepId;

  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @ColumnInfo(name = "number")
  private int number;

  @ColumnInfo(name = "description")
  private int description;


  // - - - - - - - - - - - - - - - Constructor(s) - - - - - - - - - - - - - - -

  public RecipeStep(int recipeId, int number, int description) {
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

  public int getDescription() {
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

  public void setDescription(int description) {
    this.description = description;
  }

}
