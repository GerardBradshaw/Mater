package com.gerardbradshaw.mater.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "item_table")
public class Item {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "item_id")
  private int itemId;

  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "stock_level")
  private int stockLevel;

  @ColumnInfo(name = "contains_milk")
  private String containsMilk;

  @ColumnInfo(name = "contains_egg")
  private String containsEgg;

  @ColumnInfo(name = "contains_fish")
  private String containsFish;

  @ColumnInfo(name = "contains_crustacean")
  private String containsCrustacean;

  @ColumnInfo(name = "contains_tree_nuts")
  private String containsTreeNuts;

  @ColumnInfo(name = "contains_peanuts")
  private String containsPeanuts;

  @ColumnInfo(name = "contains_wheat")
  private String containsWheat;

  @ColumnInfo(name = "contains_soy")
  private String containsSoy;

  @ColumnInfo(name = "contains_fodmap")
  private String containsFodmap;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public Item(@NonNull String name) {
    this.name = name;

    stockLevel = 0;

    // Set all allergens as not present
    String CONTAINS_NONE = ContainsAllergen.CONTAINS_NONE.name();
    containsMilk = CONTAINS_NONE;
    containsEgg = CONTAINS_NONE;
    containsFish = CONTAINS_NONE;
    containsCrustacean = CONTAINS_NONE;
    containsTreeNuts = CONTAINS_NONE;
    containsPeanuts = CONTAINS_NONE;
    containsWheat = CONTAINS_NONE;
    containsSoy = CONTAINS_NONE;
    containsFodmap = CONTAINS_NONE;

  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getItemId() {
    return itemId;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public int getStockLevel() {
    return stockLevel;
  }

  public String getContainsMilk() {
    return containsMilk;
  }

  public String getContainsEgg() {
    return containsEgg;
  }

  public String getContainsFish() {
    return containsFish;
  }

  public String getContainsCrustacean() {
    return containsCrustacean;
  }

  public String getContainsTreeNuts() {
    return containsTreeNuts;
  }

  public String getContainsPeanuts() {
    return containsPeanuts;
  }

  public String getContainsWheat() {
    return containsWheat;
  }

  public String getContainsSoy() {
    return containsSoy;
  }

  public String getContainsFodmap() {
    return containsFodmap;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public void setStockLevel(int stockLevel) {
    this.stockLevel = stockLevel;
  }

  public void setContainsMilk(String containsMilk) {
    this.containsMilk = containsMilk;
  }

  public void setContainsEgg(String containsEgg) {
    this.containsEgg = containsEgg;
  }

  public void setContainsFish(String containsFish) {
    this.containsFish = containsFish;
  }

  public void setContainsCrustacean(String containsCrustacean) {
    this.containsCrustacean = containsCrustacean;
  }

  public void setContainsTreeNuts(String containsTreeNuts) {
    this.containsTreeNuts = containsTreeNuts;
  }

  public void setContainsPeanuts(String containsPeanuts) {
    this.containsPeanuts = containsPeanuts;
  }

  public void setContainsWheat(String containsWheat) {
    this.containsWheat = containsWheat;
  }

  public void setContainsSoy(String containsSoy) {
    this.containsSoy = containsSoy;
  }

  public void setContainsFodmap(String containsFodmap) {
    this.containsFodmap = containsFodmap;
  }



  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  public enum ContainsAllergen {
    CONTAINS_NONE,
    CONTAINS_TRACES,
    CONTAINS_AS_INGREDIENT;
  };

}
