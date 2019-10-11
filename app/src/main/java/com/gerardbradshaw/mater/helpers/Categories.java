package com.gerardbradshaw.mater.helpers;

public class Categories {

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

}
