package com.gerardbradshaw.mater.helpers;

public class Categories {

  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private Categories(){
    throw new RuntimeException("DO NOT INSTANTIATE!");
  }

  public enum Category {
    FRESH_FRUIT_AND_VEG,
    BAKERY,
    CEREALS_AND_SPREADS,
    CANNED_GOODS,
    INTERNATIONAL_FOOD,
    RICE_AND_PASTA,
    COLD_FOOD,
    OILS_AND_CONDIMENTS,
    BAKING,
    SPICES,
    KITCHEN_GOODS,
    PERSONAL_HYGIENE,
    CLEANING_SUPPLIES,
    SUPPLEMENTS_AND_MEDICINES,
    BEVERAGES,
    JUICES,
    NO_CATEGORY;
  }

  public static Category getCategoryEnum(String category) {

    for (Category categoryValue : Category.values()) {
      if (categoryValue.name().equals(category)) {
        return categoryValue;
      }
    }

    return null;
  }

  public static String getCategoryString(Category category) {
    switch (category) {
      case FRESH_FRUIT_AND_VEG:
        return "Fresh fruit & vegetables";
      case BAKERY:
        return "Bakery";
      case CEREALS_AND_SPREADS:
        return "Cereals & spreads";
      case CANNED_GOODS:
        return "Canned goods";
      case INTERNATIONAL_FOOD:
        return "International food";
      case RICE_AND_PASTA:
        return "Rice & pasta";
      case COLD_FOOD:
        return "Cold food";
      case OILS_AND_CONDIMENTS:
        return "Oils & condiments";
      case BAKING:
        return "Baking";
      case SPICES:
        return "Spices";
      case KITCHEN_GOODS:
        return "Kitchen supplies";
      case PERSONAL_HYGIENE:
        return "Personal hygiene";
      case CLEANING_SUPPLIES:
        return "Cleaning supplies";
      case SUPPLEMENTS_AND_MEDICINES:
        return "Supplements & medicines";
      case BEVERAGES:
        return "Beverages";
      case JUICES:
        return "Juices";
      default:
        return "Uncategorised";
    }
  }

  public static String getCategoryString(String category) {
    return getCategoryString(getCategoryEnum(category));
  }

}
