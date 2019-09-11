package com.gerardbradshaw.tomatoes;

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
  public String volume(double amount, Volume from, Volume to) {

    double mlAmount = convertToMl(amount, from);
    double newAmount = convertFromMl(mlAmount, to);
    String newAmountString = String.valueOf(newAmount);

    String unit;

    String ml = " mL";
    String cups = " cups";
    String tsp = " tsp";
    String tbsp = " tbsp";
    String usCups = " US cups";
    String qt = " qt";
    String usTsp = " US tsp";
    String usTbsp = " US tbsp";
    String flOz = " flOz";
    switch (to) {
      case MILLILITRES:
        unit = ml;
      case AU_CUPS:
        unit = cups;
      case AU_TEASPOONS:
        unit = tsp;
      case AU_TABLESPOONS:
        unit = tbsp;
      case US_CUPS:
        unit = usCups;
      case FLUID_OUNCES:
        unit = flOz;
      case QUARTS:
        unit = qt;
      case US_TEASPOONS:
        unit = usTsp;
      case US_TABLESPOONS:
        unit = usTbsp;
      default:
        unit = "";
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
  public String mass(double amount, Mass from, Mass to) {

    double gramAmount = convertToGrams(amount, from);
    double newAmount = convertFromGrams(gramAmount, to);
    String newAmountString = String.valueOf(newAmount);

    String unit;

    String grams = " grams";
    String kg = " kgs";
    String oz = " oz";
    String lbs = " lbs";
    switch (to) {
      case GRAMS:
        unit = grams;
      case KILOGRAMS:
        unit = kg;
      case OUNCES:
        unit = oz;
      case POUNDS:
        unit = lbs;
      default:
        unit = "";
    }

    return newAmountString + unit;
  }



  // - - - - - - - - - - - - - - - Private methods - - - - - - - - - - - - - - -

  private double convertToMl(double amount, Volume originalUnit) {
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

  private double convertFromMl(double amount, Volume desiredUnit) {
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

  private double convertToGrams(double amount, Mass originalUnit) {
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

  private double convertFromGrams(double amount, Mass desiredUnit) {
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
