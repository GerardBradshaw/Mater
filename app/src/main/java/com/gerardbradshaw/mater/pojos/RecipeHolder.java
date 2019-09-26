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
  private List<RecipeIngredientHolder> recipeIngredients = new ArrayList<>();


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

  public void setRecipeIngredients(List<RecipeIngredientHolder> recipeIngredientHolders) {
    this.recipeIngredients = recipeIngredientHolders;
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

  public List<RecipeIngredientHolder> getRecipeIngredients() {
    return recipeIngredients;
  }


  // - - - - - - - - - - - - - - - Other step methods - - - - - - - - - - - - - - -

  public String getStep(int stepNumber) {

    // Set the index of the step
    int stepsIndex = stepNumber - 1;

    // Return the step if it exists
    if (steps.size() < stepNumber) {
      return steps.get(stepsIndex);

    } else {
      return "";
    }
  }

  public void addNewStep(String step) {
    steps.add(step);
  }

  public boolean deleteStep(int stepNumber) {

    // Set the index in the steps array which will be deleted.
    int stepsIndex = stepNumber - 1;

    // Delete the step if it exists and return true.
    if (steps.size() < stepNumber) {
      steps.remove(stepsIndex);
      return true;

    // Otherwise do nothing and return false.
    } else {
      return false;
    }
  }

  public boolean replaceStep(int stepNumber, String newStep) {

    // Set the index in the steps array which will be replaced.
    int stepsIndex = stepNumber - 1;

    // Replace the step if it exists
    if (steps.size() < stepNumber) {
      steps.set(stepsIndex, newStep);
      return true;

    // Otherwise do nothing and return false.
    } else {
      return false;
    }
  }


  // - - - - - - - - - - - - - - - Other ingredient methods - - - - - - - - - - - - - - -

  public void addIngredient(RecipeIngredientHolder recipeIngredientHolder) {
    recipeIngredients.add(recipeIngredientHolder);
  }

}
