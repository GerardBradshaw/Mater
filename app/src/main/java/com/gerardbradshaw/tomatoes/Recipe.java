package com.gerardbradshaw.tomatoes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "recipe_table")
public class Recipe {


  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "description")
  private String description;

  @ColumnInfo(name = "ingredients")
  private List<Ingredient> ingredients;

  @ColumnInfo(name = "steps")
  private List<String> steps;

  @ColumnInfo(name = "quantities")
  private Map<Ingredient, Quantity> quantities;


  // - - - - - - - - - - - - - - - Constructors - - - - - - - - - - - - - - -

  // Default constructor
  public Recipe(@NonNull String title) {
    this.title = title;
  }

  // Secondary constructor
  @Ignore
  public Recipe(@NonNull String title, String description, List<Ingredient> ingredients,
                List<String> steps, Map<Ingredient, Quantity> quantities) {

    this.title = title;
    this.description = description;
    this.ingredients = ingredients;
    this.steps = steps;
    this.quantities = quantities;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  @NonNull
  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public List<String> getSteps() {
    return steps;
  }

  public Map<Ingredient, Quantity> getQuantities() {
    return quantities;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public void setSteps(List<String> steps) {
    this.steps = steps;
  }

  public void setQuantities(Map<Ingredient, Quantity> quantities) {
    this.quantities =  quantities;
  }

}
