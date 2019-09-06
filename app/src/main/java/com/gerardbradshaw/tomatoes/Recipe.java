package com.gerardbradshaw.tomatoes;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private int id;
  private String title;
  private String description;
  private List<String> steps;
  private List<Object[]> ingredients;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public Recipe() {
    ingredients = new ArrayList<>();
    steps = new ArrayList<>();
  }


  // - - - - - - - - - - - - - - - Summary methods - - - - - - - - - - - - - - -

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


  // - - - - - - - - - - - - - - - Step methods - - - - - - - - - - - - - - -

  /**
   * Getter method for all the steps
   *
   * @return List<String>: The steps.
   */
  public List<String> getSteps() {
    return steps;
  }

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
   * Setter method for steps.
   *
   * @param steps List<String>: The steps.
   */
  public void setSteps(List<String> steps) {
    this.steps = steps;
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


  // - - - - - - - - - - - - - - - Ingredient methods - - - - - - - - - - - - - - -

  public void addIngredient(String name, double amount, String units) {

    Object[] newIngredient = {name, amount, units};

    ingredients.add(newIngredient);

  }

}
