package com.gerardbradshaw.mater.activities.addrecipe;

import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.RecipeIngredient;

public class AddIngredientItemHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private Ingredient ingredient;
  private RecipeIngredient recipeIngredient;


  // - - - - - - - - - - - - - - - Constructors - - - - - - - - - - - - - - -


  public AddIngredientItemHolder(Ingredient ingredient, RecipeIngredient recipeIngredient) {
    this.ingredient = ingredient;
    this.recipeIngredient = recipeIngredient;
  }

  public AddIngredientItemHolder() {
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public Ingredient getIngredient() {
    return ingredient;
  }

  public RecipeIngredient getRecipeIngredient() {
    return recipeIngredient;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setIngredient(Ingredient ingredient) {
    this.ingredient = ingredient;
  }

  public void setRecipeIngredient(RecipeIngredient recipeIngredient) {
    this.recipeIngredient = recipeIngredient;
  }
  
}
