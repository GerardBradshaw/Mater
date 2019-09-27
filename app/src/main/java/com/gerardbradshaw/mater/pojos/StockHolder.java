package com.gerardbradshaw.mater.pojos;

import com.gerardbradshaw.mater.room.entities.Ingredient;

public class StockHolder {

  private Ingredient ingredient;
  private boolean inStock;

  public StockHolder(Ingredient ingredient, boolean inStock) {
    this.ingredient = ingredient;
    this.inStock = inStock;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public void setIngredient(Ingredient ingredient) {
    this.ingredient = ingredient;
  }

  public boolean isInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }
}
