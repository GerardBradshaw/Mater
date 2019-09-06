package com.gerardbradshaw.tomatoes.holders;

import java.util.ArrayList;
import java.util.List;

public class RecipeHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private int id;
  private String title;
  private String description;
  private List<String> steps;
  private List<RecipeIngredientHolder> recipeIngredients;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeHolder() {
    recipeIngredients = new ArrayList<>();
    steps = new ArrayList<>();
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  /**
   * Setter method for title.
   *
   * @param title, String: The title.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Setter method for description.
   *
   * @param description, String: The description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Setter method for steps.
   *
   * @param steps List<String>: The steps.
   */
  public void setSteps(List<String> steps) {
    this.steps = steps;
  }

  /**
   * Setter method for recipeIngredients.
   *
   * @param recipeIngredientHolders: The recipeIngredients.
   */
  public void setRecipeIngredients(List<RecipeIngredientHolder> recipeIngredientHolders) {
    this.recipeIngredients = recipeIngredientHolders;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  /**
   * Getter for title.
   *
   * @return String: The title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Getter for description.
   *
   * @return String: the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Getter method for all the steps
   *
   * @return List<String>: The steps.
   */
  public List<String> getSteps() {
    return steps;
  }

  /**
   * Getter for recipeIngredients.
   *
   * @return List<IngredientHolder>: the recipeIngredients.
   */
  public List<RecipeIngredientHolder> getRecipeIngredients() {
    return recipeIngredients;
  }


  // - - - - - - - - - - - - - - - Other step methods - - - - - - - - - - - - - - -

  /**
   * Gets the specified step.
   *
   * @param stepNumber, int: The step number to return.
   * @return String: The step.
   */
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

  /**
   * Adds a new step at the end of the list.
   *
   * @param step, String: The next step to be added.
   */
  public void addNewStep(String step) {
    steps.add(step);
  }

  /**
   * Deletes a step.
   *
   * @param stepNumber, int: The step to be deleted.
   * @return boolean: TRUE if the step number exists and was deleted, FALSE otherwise.
   */
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

  /**
   * Replaces the specified step.
   *
   * @param stepNumber, int: The step number to be replaced.
   * @param newStep, String: The new step.
   * @return boolean: TRUE if the step number exists and was replaced, FALSE otherwise.
   */
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

  /**
   * Adds an ingredient to the recipe.
   *
   * @param recipeIngredientHolder: The ingredient to be added.
   */
  public void addIngredient(RecipeIngredientHolder recipeIngredientHolder) {
    recipeIngredients.add(recipeIngredientHolder);
  }

}
