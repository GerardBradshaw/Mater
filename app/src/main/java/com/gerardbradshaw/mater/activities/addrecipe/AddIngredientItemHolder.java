package com.gerardbradshaw.mater.activities.addrecipe;

import com.gerardbradshaw.mater.room.entities.Item;
import com.gerardbradshaw.mater.room.entities.RecipeIngredient;

public class AddIngredientItemHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private Item item;
  private RecipeIngredient recipeIngredient;


  // - - - - - - - - - - - - - - - Constructors - - - - - - - - - - - - - - -


  public AddIngredientItemHolder(Item item, RecipeIngredient recipeIngredient) {
    this.item = item;
    this.recipeIngredient = recipeIngredient;
  }

  public AddIngredientItemHolder() {
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public Item getItem() {
    return item;
  }

  public RecipeIngredient getRecipeIngredient() {
    return recipeIngredient;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setItem(Item item) {
    this.item = item;
  }

  public void setRecipeIngredient(RecipeIngredient recipeIngredient) {
    this.recipeIngredient = recipeIngredient;
  }

}
