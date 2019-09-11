package com.gerardbradshaw.tomatoes.helpers;

import com.gerardbradshaw.tomatoes.helpers.Units.Mass;
import com.gerardbradshaw.tomatoes.helpers.Units.Volume;
import com.gerardbradshaw.tomatoes.helpers.Units.NoUnits;

import java.util.Locale;

public class UnitFormatter {

  static boolean isMetric = true;  // TODO add this to shared preferences

  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private UnitFormatter() {
    throw new RuntimeException("DO NOT INSTANTIATE!");
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public static String forDetailView(double amount, Volume unit) {

    Volume newUnit;

    if(isMetric) {
      switch (unit) {
        case US_CUPS:
          newUnit = Volume.AU_CUPS;
        case FLUID_OUNCES:
          newUnit = Volume.MILLILITRES;
        case QUARTS:
          newUnit = Volume.AU_CUPS;
        case US_TEASPOONS:
          newUnit = Volume.AU_TEASPOONS;
        case US_TABLESPOONS:
          newUnit = Volume.AU_TABLESPOONS;
        default:
          newUnit = unit;
      }
    } else {
      switch (unit) {
        case MILLILITRES:
          newUnit = Volume.FLUID_OUNCES;
        case AU_CUPS:
          newUnit = Volume.US_CUPS;
        case AU_TEASPOONS:
          newUnit = Volume.US_TEASPOONS;
        case AU_TABLESPOONS:
          newUnit = Volume.US_TABLESPOONS;
        default:
          newUnit = unit;
      }
    }
    return Units.convertVolume(amount, unit, newUnit);
  }

  public static String forDetailView(double amount, Mass unit) {

    Mass newUnit;

    if (isMetric) {
      switch (unit) {
        case OUNCES:
          newUnit = Mass.GRAMS;
        case POUNDS:
          newUnit = Mass.GRAMS;
        default:
          newUnit = unit;
      }
    } else {
      switch (unit) {
        case GRAMS:
          newUnit = Mass.OUNCES;
        case KILOGRAMS:
          newUnit = Mass.POUNDS;
        default:
          newUnit = unit;
      }
    }
    return Units.convertMass(amount, unit, newUnit);
  }

  public static String forDetailView(double amount, NoUnits unit) {

    String amountString = String.format(Locale.getDefault(), "%.0f", amount);

    switch (unit) {
      case DROPS:
        return amountString + " drops ";
      case PINCH:
        return amountString + " pinch ";
      default:
        return amountString + " ";
    }
  }

  public static String forDetailView(double amount, String unit) {

    try {
      Volume volumeUnit = Volume.valueOf(unit);
      return forDetailView(amount, volumeUnit);

    } catch (IllegalArgumentException notVolume) {
      try {
        Mass massUnit = Mass.valueOf(unit);
        return forDetailView(amount, massUnit);

      } catch (IllegalArgumentException notMass) {
        try {
          NoUnits noUnit = NoUnits.valueOf(unit);
          return forDetailView(amount, noUnit);

        } catch (IllegalArgumentException notNoUnit) {
          return "Units error";
        }
      }
    }
  }
}
