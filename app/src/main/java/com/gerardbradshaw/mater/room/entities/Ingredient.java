package com.gerardbradshaw.mater.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gerardbradshaw.mater.helpers.Units;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "ingredient_table",
    foreignKeys = {
    @ForeignKey(entity = Summary.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = CASCADE),
    @ForeignKey(entity = Item.class,
        parentColumns = "item_id",
        childColumns = "item_id",
        onDelete = CASCADE)},
    indices = {@Index(value = "recipe_id"), @Index(value = "item_id")})
public class Ingredient {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "ingredient_id")
  private int ingredientId;

  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @ColumnInfo(name = "item_id")
  private int itemId;

  @ColumnInfo(name = "amount")
  private double amount;

  @ColumnInfo(name = "units")
  private String units;


  // - - - - - - - - - - - - - - - Constructor(s) - - - - - - - - - - - - - - -

  public Ingredient(int recipeId, int itemId, double amount, String units) {
    this.recipeId = recipeId;
    this.itemId = itemId;
    this.amount = amount;
    this.units = units;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getIngredientId() {
    return ingredientId;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public int getItemId() {
    return itemId;
  }

  public double getAmount() {
    return amount;
  }

  public String getUnits() {
    return units;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -


  public void setIngredientId(int ingredientId) {
    this.ingredientId = ingredientId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
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
