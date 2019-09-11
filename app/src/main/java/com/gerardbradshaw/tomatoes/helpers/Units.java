package com.gerardbradshaw.tomatoes.helpers;

import java.util.Locale;

public class Units {

  static boolean isMetric = true;  // TODO add this to shared preferences


  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private Units(){
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


  // - - - - - - - - - - - - - - - Private methods - - - - - - - - - - - - - - -

  /**
   * Converts a volume between units.
   *
   * @param amount the absolute amount in the original unit
   * @param from the original unit
   * @param to the new unit
   * @return the volume expressed in the new unit
   */
  private static String convertVolume(double amount, Volume from, Volume to) {

    double mlAmount = convertToMl(amount, from);
    double newAmount = convertFromMl(mlAmount, to);
    String format;

    String unit;

    switch (to) {
      case MILLILITRES:
        unit = " mL ";
        format = "%.0f";
        break;
      case AU_CUPS:
        unit = " cups ";
        format = "%.2f";
        break;
      case AU_TEASPOONS:
        unit = " tsp ";
        format = "%.2f";
        break;
      case AU_TABLESPOONS:
        unit = " tbsp ";
        format = "%.2f";
        break;
      case US_CUPS:
        unit = " US cups ";
        format = "%.2f";
        break;
      case FLUID_OUNCES:
        unit = " flOz ";
        format = "%.2f";
        break;
      case QUARTS:
        unit = " qt ";
        format = "%.2f";
        break;
      case US_TEASPOONS:
        unit = " US tsp ";
        format = "%.2f";
        break;
      case US_TABLESPOONS:
        unit = " US tbsp ";
        format = "%.2f";
        break;
      default:
        unit = " ";
        format = "%.0f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + unit;

  }

  /**
   * Converts a mass between units.
   *
   * @param amount the absolute amount in the original unit
   * @param from the original unit
   * @param to the new unit
   * @return the mass expressed in the new unit
   */
  private static String convertMass(double amount, Mass from, Mass to) {

    double gramAmount = convertToGrams(amount, from);
    double newAmount = convertFromGrams(gramAmount, to);
    String format;

    String unit;

    switch (to) {
      case GRAMS:
        unit = " grams ";
        format = "%.0f";
        break;
      case KILOGRAMS:
        unit = " kgs ";
        format = "%.2f";
        break;
      case OUNCES:
        unit = " oz ";
        format = "%.1f";
        break;
      case POUNDS:
        unit = " lbs ";
        format = "%.2f";
        break;
      default:
        unit = " ";
        format = "%.0f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + unit;
  }

  private static double convertToMl(double amount, Volume originalUnit) {
    switch (originalUnit) {
      case MILLILITRES:
        return amount;
      case AU_CUPS:
        return amount * 250;
      case AU_TEASPOONS:
        return amount * 5;
      case AU_TABLESPOONS:
        return amount * 20;
      case US_CUPS:
        return amount * 236.588;
      case FLUID_OUNCES:
        return amount * 29.574;
      case QUARTS:
        return amount * 946.353;
      case US_TEASPOONS:
        return amount * 4.92892;
      case US_TABLESPOONS:
        return amount * 14.7868;
      default:
        return 0;
    }
  }

  private static double convertFromMl(double amount, Volume desiredUnit) {
    switch (desiredUnit) {
      case MILLILITRES:
        return amount;
      case AU_CUPS:
        return amount / 250;
      case AU_TEASPOONS:
        return amount / 5;
      case AU_TABLESPOONS:
        return amount / 20;
      case US_CUPS:
        return amount / 236.588;
      case FLUID_OUNCES:
        return amount / 29.574;
      case QUARTS:
        return amount / 946.353;
      case US_TEASPOONS:
        return amount / 4.92892;
      case US_TABLESPOONS:
        return amount / 14.7868;
      default:
        return 0;
    }
  }

  private static double convertToGrams(double amount, Mass originalUnit) {
    switch (originalUnit) {
      case GRAMS:
        return amount;
      case KILOGRAMS:
        return amount * 1000;
      case OUNCES:
        return amount * 28.3495;
      case POUNDS:
        return amount * 453.592;
      default:
        return 0;
    }
  }

  private static double convertFromGrams(double amount, Mass desiredUnit) {
    switch (desiredUnit) {
      case GRAMS:
        return amount;
      case KILOGRAMS:
        return amount / 1000;
      case OUNCES:
        return amount / 28.3495;
      case POUNDS:
        return amount / 453.592;
      default:
        return 0;
    }
  }


  // - - - - - - - - - - - - - - - Enums - - - - - - - - - - - - - - -

  public enum Volume {
    MILLILITRES,
    AU_CUPS,
    AU_TEASPOONS,
    AU_TABLESPOONS,
    US_CUPS,
    FLUID_OUNCES,
    QUARTS,
    US_TEASPOONS,
    US_TABLESPOONS;
  }

  public enum Mass {
    GRAMS,
    KILOGRAMS,
    POUNDS,
    OUNCES;
  }

  public enum NoUnits {
    DROPS,
    NO_UNIT,
    PINCH;
  }

}
