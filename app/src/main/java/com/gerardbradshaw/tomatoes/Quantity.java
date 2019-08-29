package com.gerardbradshaw.tomatoes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quantity_table")
public class Quantity {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "uid")
  private int uid;

  @ColumnInfo(name = "value")
  private double value;

  @ColumnInfo(name = "metricUnit")
  private String metricUnit;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public Quantity(double value, Unit unit) {
    this.value = value;
    this.metricUnit = unit.name();
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getUid() {
    return uid;
  }

  public double getValue() {
    return value;
  }

  public Unit getMetricUnit() {
    return Unit.valueOf(metricUnit);
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setUid(int uid) {
    this.uid = uid;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public void setMetricUnit(Unit unit) {
    this.metricUnit = unit.name();
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -



  public enum Unit {
    MILLILITRES,
    DROPS,
    GRAMS,
    KILOGRAMS,
    METRIC_CUPS,
    TEASPOONS,
    TABLESPOONS,
    PINCH,
    NO_UNIT;
  };

}
