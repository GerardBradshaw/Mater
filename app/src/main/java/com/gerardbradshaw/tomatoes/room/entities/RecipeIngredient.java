package com.gerardbradshaw.tomatoes.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gerardbradshaw.tomatoes.helpers.Units;

@Entity(tableName = "recipe_ingredient_table",

    foreignKeys = {
    @ForeignKey(entity = Summary.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id"),

    @ForeignKey(entity = Ingredient.class,
        parentColumns = "ingredient_id",
        childColumns = "ingredient_id")},

    indices = {@Index(value = "recipe_id"), @Index(value = "ingredient_id")})
public class RecipeIngredient {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "recipe_ingredient_id")
  private int recipeIngredientId;

  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @ColumnInfo(name = "ingredient_id")
  private int ingredientId;

  @ColumnInfo(name = "amount")
  private double amount;

  @ColumnInfo(name = "units")
  private String units;


  // - - - - - - - - - - - - - - - Constructor(s) - - - - - - - - - - - - - - -

  public RecipeIngredient(int recipeId, int ingredientId, double amount, String units) {
    this.recipeId = recipeId;
    this.ingredientId = ingredientId;
    this.amount = amount;
    this.units = units;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getRecipeIngredientId() {
    return recipeIngredientId;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public int getIngredientId() {
    return ingredientId;
  }

  public double getAmount() {
    return amount;
  }

  public String getUnits() {
    return units;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -


  public void setRecipeIngredientId(int recipeIngredientId) {
    this.recipeIngredientId = recipeIngredientId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public void setIngredientId(int ingredientId) {
    this.ingredientId = ingredientId;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setUnits(Units.Volume units) {
    this.units = units.name();
  }

  public void setUnits(Units.Mass units) {
    this.units = units.name();
  }

  public void setUnits(Units.MiscUnits units) {
    this.units = units.name();
  }

}
