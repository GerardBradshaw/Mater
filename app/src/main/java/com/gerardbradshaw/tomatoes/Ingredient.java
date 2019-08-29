package com.gerardbradshaw.tomatoes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;

@Entity(tableName = "ingredients_table")
public class Ingredient {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "containsMilk")
  private int containsMilk;

  @ColumnInfo(name = "containsEgg")
  private int containsEgg;

  @ColumnInfo(name = "containsFish")
  private int containsFish;

  @ColumnInfo(name = "containsCrustacean")
  private int containsCrustacean;

  @ColumnInfo(name = "containsTreeNuts")
  private int containsTreeNuts;

  @ColumnInfo(name = "containsPeanuts")
  private int containsPeanuts;

  @ColumnInfo(name = "containsWheat")
  private int containsWheat;

  @ColumnInfo(name = "containsSoy")
  private int containsSoy;

  @ColumnInfo(name = "containsFodmap")
  private int containsFodmap;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public Ingredient(@NonNull String name) {
    this.name = name;

    // Set all allergens as not present
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

  @NonNull
  public String getName() {
    return name;
  }

  public int getContainsMilk() {
    return containsMilk;
  }

  public int getContainsEgg() {
    return containsEgg;
  }

  public int getContainsFish() {
    return containsFish;
  }

  public int getContainsCrustacean() {
    return containsCrustacean;
  }

  public int getContainsTreeNuts() {
    return containsTreeNuts;
  }

  public int getContainsPeanuts() {
    return containsPeanuts;
  }

  public int getContainsWheat() {
    return containsWheat;
  }

  public int getContainsSoy() {
    return containsSoy;
  }

  public int getContainsFodmap() {
    return containsFodmap;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public void setContainsMilk(ContainsAllergen containsMilk) {
    this.containsMilk = containsMilk.index;
  }

  public void setContainsEgg(ContainsAllergen containsEgg) {
    this.containsEgg = containsEgg.index;
  }

  public void setContainsFish(ContainsAllergen containsFish) {
    this.containsFish = containsFish.index;
  }

  public void setContainsCrustacean(ContainsAllergen containsCrustacean) {
    this.containsCrustacean = containsCrustacean.index;
  }

  public void setContainsTreeNuts(ContainsAllergen containsTreeNuts) {
    this.containsTreeNuts = containsTreeNuts.index;
  }

  public void setContainsPeanuts(ContainsAllergen containsPeanuts) {
    this.containsPeanuts = containsPeanuts.index;
  }

  public void setContainsWheat(ContainsAllergen containsWheat) {
    this.containsWheat = containsWheat.index;
  }

  public void setContainsSoy(ContainsAllergen containsSoy) {
    this.containsSoy = containsSoy.index;
  }

  public void setContainsFodmap(ContainsAllergen containsFodmap) {
    this.containsFodmap = containsFodmap.index;
  }



  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -


  @Ignore
  private static final int CONTAINS_NONE = 0;

  @Ignore
  public static final int CONTAINS_TRACES = 1;

  @Ignore
  public static final int CONTAINS_AS_INGREDIENT = 2;

  private enum ContainsAllergen {
    CONTAINS_NONE(0),
    CONTAINS_TRACES(1),
    CONTAINS_AS_INGREDIENT(2);

    private final int index;

    ContainsAllergen(int index) {
      this.index = index;
    }
  };

}
