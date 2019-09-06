package com.gerardbradshaw.tomatoes;

import android.app.Application;

import com.gerardbradshaw.tomatoes.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.holders.RecipeHolder;
import com.gerardbradshaw.tomatoes.holders.RecipeIngredientHolder;

import java.util.ArrayList;
import java.util.List;

public class TomatoesApplication extends Application {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private SharedPrefHelper sharedPrefHelper;


  // - - - - - - - - - - - - - - - Application methods - - - - - - - - - - - - - - -

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize shared prefs
    sharedPrefHelper = new SharedPrefHelper(this);

    // Check if the application has been launched before. If not, create some recipes.
    if (sharedPrefHelper.isFirstLaunch()) {

      // Updated the firstLaunched status
      sharedPrefHelper.setAsLaunched();

      // Create a new RecipeHolder object
      RecipeHolder lasagneRecipeHolder = new RecipeHolder();

      // Set the title and description of the recipe
      lasagneRecipeHolder.setTitle("Vegan Lasagne");
      lasagneRecipeHolder.setDescription("A delicious comfort food that will leave you thinking \"I CAN'T BELIEVE THIS IS VEGAN!");

      // Create the cooking steps
      List<String> steps = new ArrayList<>();
      steps.add("Dice the sweet potato, zucchini, and capsicum into small cubes.");
      steps.add("Sauté diced vegetables in large fry pan on medium-high temperature for 10 minutes or until sweet potato has softened.");
      steps.add("Add diced tomatoes and Beyond burgers to pan. Break up the burger patties and combine.");
      steps.add("Add red wine and set pan to simmer, stirring occasionally.");
      steps.add("While vegetables are simmering, line baking dish with lasagne sheets and place Vegenaise into a snap-lock bag.");
      steps.add("When most of the juice has boiled away (some juice is desirable to properly soften the lasagne sheets), use a spoon to spread a thin layer of the mix over the lasagne sheets.");
      steps.add("Cover the mix with cheese. Add another 2 layers of lasagne sheets, vegetable mix, and cheese as before.");
      steps.add("Make a small cut in the corner of the snap-lock bag and squeeze (pipe) the vegenaise over the top layer of cheese.");
      steps.add("Bake for ~30 minutes at 215°C (420°F) or until golden brown on top.");
      steps.add("Allow lasagne to cool for 5-10 minutes and slice into desired portion sizes.");
      steps.add("Enjoy!");

      // Add the steps to the recipe
      lasagneRecipeHolder.setSteps(steps);

      // Create a RecipeIngredientHolder object
      List<RecipeIngredientHolder> ingredients = new ArrayList<>();

      // Define the names of ingredients that contain allergens
      String beyondBurgers = "Beyond burgers";
      String lasagneSheets = "lasagne sheets";
      String vegenaise = "Vegenaise";

      // Add each ingredient to the list
      ingredients.add(new RecipeIngredientHolder(
          "sweet potato", 800d, RecipeIngredientHolder.Unit.GRAMS));

      ingredients.add(new RecipeIngredientHolder(
          "sweet potato", 800d, RecipeIngredientHolder.Unit.GRAMS));

      ingredients.add(new RecipeIngredientHolder(
          "capsicum", 1d, RecipeIngredientHolder.Unit.NO_UNIT));

      ingredients.add(new RecipeIngredientHolder(
          "zucchini", 1d, RecipeIngredientHolder.Unit.NO_UNIT));

      ingredients.add(new RecipeIngredientHolder(
          "frozen spinach", 100d, RecipeIngredientHolder.Unit.GRAMS));

      ingredients.add(new RecipeIngredientHolder(
          "diced tomatoes", 800d, RecipeIngredientHolder.Unit.GRAMS));

      ingredients.add(new RecipeIngredientHolder(
          beyondBurgers, 4d, RecipeIngredientHolder.Unit.NO_UNIT));

      ingredients.add(new RecipeIngredientHolder(
          "merlot", 500d, RecipeIngredientHolder.Unit.MILLILITRES));

      ingredients.add(new RecipeIngredientHolder(
          lasagneSheets, 1d, RecipeIngredientHolder.Unit.NO_UNIT));

      ingredients.add(new RecipeIngredientHolder(
          "vegan cheese slices", 18d, RecipeIngredientHolder.Unit.NO_UNIT));

      ingredients.add(new RecipeIngredientHolder(
          vegenaise, 100d, RecipeIngredientHolder.Unit.GRAMS));

      // Add the RecipeIngredients to the Recipe
      lasagneRecipeHolder.setRecipeIngredients(ingredients);

      // Save the recipe to the repository

    }
  }
}
