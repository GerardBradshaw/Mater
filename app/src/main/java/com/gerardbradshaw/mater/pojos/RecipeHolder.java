package com.gerardbradshaw.mater.pojos;

import java.util.ArrayList;
import java.util.List;

public class RecipeHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private String title;
  private String description;
  private int servings;
  private String imageDirectory;
  private List<String> steps = new ArrayList<>();
  private List<IngredientHolder> ingredientHolders = new ArrayList<>();


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  // Empty constructor
  public RecipeHolder() {
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -


  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public void setImageDirectory(String imageDirectory) {
    this.imageDirectory = imageDirectory;
  }

  public void setSteps(List<String> steps) {
    this.steps = steps;
  }

  public void setIngredientHolders(List<IngredientHolder> ingredientHolders) {
    this.ingredientHolders = ingredientHolders;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public int getServings() {
    return servings;
  }

  public String getImageDirectory() {
    return imageDirectory;
  }

  public List<String> getSteps() {
    return steps;
  }

  public List<IngredientHolder> getIngredientHolders() {
    return ingredientHolders;
  }

}
