package com.gerardbradshaw.mater.pojos;

import com.gerardbradshaw.mater.room.entities.Item;

public class StockHolder {

  private Item item;
  private boolean inStock;

  public StockHolder(Item item, boolean inStock) {
    this.item = item;
    this.inStock = inStock;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public boolean isInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }
}
