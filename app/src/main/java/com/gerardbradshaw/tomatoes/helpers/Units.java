package com.gerardbradshaw.tomatoes.helpers;

import java.util.Locale;

public class Units {

  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private Units(){
    throw new RuntimeException("DO NOT INSTANTIATE!");
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  /**
   * Converts a volume between units.
   *
   * @param amount the absolute amount in the original unit
   * @param from the original unit
   * @param to the new unit
   * @return the volume expressed in the new unit
   */
  public static String convertVolume(double amount, Volume from, Volume to) {

    double mlAmount = convertToMl(amount, from);
    double newAmount = convertFromMl(mlAmount, to);
    String newAmountString = String.format(Locale.getDefault(), "%.1f", newAmount);

    String unit;

    switch (to) {
      case MILLILITRES:
        unit = " mL ";
        break;
      case AU_CUPS:
        unit = " cups ";
        break;
      case AU_TEASPOONS:
        unit = " tsp ";
        break;
      case AU_TABLESPOONS:
        unit = " tbsp ";
        break;
      case US_CUPS:
        unit = " US cups ";
        break;
      case FLUID_OUNCES:
        unit = " flOz ";
        break;
      case QUARTS:
        unit = " qt ";
        break;
      case US_TEASPOONS:
        unit = " US tsp ";
        break;
      case US_TABLESPOONS:
        unit = " US tbsp ";
        break;
      default:
        unit = " ";
    }

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
  public static String convertMass(double amount, Mass from, Mass to) {

    double gramAmount = convertToGrams(amount, from);
    double newAmount = convertFromGrams(gramAmount, to);
    String newAmountString = String.format(Locale.getDefault(), "%.1f", newAmount);

    String unit;

    switch (to) {
      case GRAMS:
        unit = " grams ";
        break;
      case KILOGRAMS:
        unit = " kgs ";
        break;
      case OUNCES:
        unit = " oz ";
        break;
      case POUNDS:
        unit = " lbs ";
        break;
      default:
        unit = " ";
    }

    return newAmountString + unit;
  }


  // - - - - - - - - - - - - - - - Private methods - - - - - - - - - - - - - - -

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
