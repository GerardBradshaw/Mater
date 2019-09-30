package com.gerardbradshaw.mater.pojos;

import com.gerardbradshaw.mater.helpers.Units;
import com.gerardbradshaw.mater.helpers.Units.Mass;
import com.gerardbradshaw.mater.helpers.Units.Volume;
import com.gerardbradshaw.mater.helpers.Units.MiscUnits;

public class RecipeIngredientHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private String name;
  private double amount;
  private String unit;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeIngredientHolder(String name, double amount, Volume unit) {
    this.name = name;
    this.amount = amount;
    this.unit = unit.name();
  }

  public RecipeIngredientHolder(String name, double amount, Mass unit) {
    this.name = name;
    this.amount = amount;
    this.unit = unit.name();
  }

  public RecipeIngredientHolder(String name, double amount, MiscUnits unit) {
    this.name = name;
    this.amount = amount;
    this.unit = unit.name();
  }

  public RecipeIngredientHolder(String name, double amount, String unit) {
    if (Units.getVolumeEnum(unit) != null || Units.getMassEnum(unit) != null
        || Units.getMiscUnitsEnum(unit) != null) {
      this.unit = unit;

    } else {
      this.unit = MiscUnits.NO_UNIT.name();
    }

    this.name = name;
    this.amount = amount;
  }

  public RecipeIngredientHolder() {
    name = "";
    amount = 0;
    unit = "";
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public String getName() {
    return name;
  }

  public double getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setName(String name) {
    this.name = name;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }
}
