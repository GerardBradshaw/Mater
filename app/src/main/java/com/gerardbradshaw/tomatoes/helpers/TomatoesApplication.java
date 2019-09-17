package com.gerardbradshaw.tomatoes.helpers;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.helpers.Units.Mass;
import com.gerardbradshaw.tomatoes.helpers.Units.Volume;
import com.gerardbradshaw.tomatoes.helpers.Units.MiscUnits;

import java.util.ArrayList;
import java.util.List;

public class TomatoesApplication extends Application {

  private RecipeRepository repository;

  // - - - - - - - - - - - - - - - Application methods - - - - - - - - - - - - - - -

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize the repository
    repository = new RecipeRepository(this);

    // Initialize shared prefs
    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);

    // Check if the application has been launched before. If not, create some recipes.
    if (sharedPrefHelper.isFirstLaunch()) {

      // Updated the firstLaunched status
      sharedPrefHelper.setAsLaunched();

      // Create the default recipes
      RecipeHolder lasagneRecipeHolder = createLasagneRecipe();
      RecipeHolder curryRecipeHolder = createCurryRecipe();

      // Add the recipes to the database
      repository.insertRecipeFromHolder(lasagneRecipeHolder);
      repository.insertRecipeFromHolder(curryRecipeHolder);

      // Store the lasagne image
      Bitmap lasagneImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_lasagne);
      repository.saveImage(lasagneRecipeHolder.getTitle(), lasagneImage);

      // Store the curry image
      //Bitmap curryImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_curry);
      //repository.saveImage(curryRecipeHolder.getTitle(), curryImage);

    }
  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  /**
   * Creates a lasagne RecipeHolder.
   *
   * @return RecipeHolder: The lasagne recipe.
   */
  private RecipeHolder createLasagneRecipe() {

    // Create a new RecipeHolder object
    RecipeHolder recipe = new RecipeHolder();

    // Set the title and description of the recipe
    recipe.setTitle("Vegan Lasagne");
    recipe.setDescription("A delicious comfort food that will leave you thinking \"I CAN'T BELIEVE THIS IS VEGAN!");

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
    recipe.setSteps(steps);

    // Create a RecipeIngredientHolder object
    List<RecipeIngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String beyondBurgers = "Beyond burgers";
    String lasagneSheets = "lasagne sheets";
    String vegenaise = "Vegenaise";

    // Add each ingredient to the list
    ingredients.add(new RecipeIngredientHolder(
        "sweet potato", 800d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "capsicum", 1d, MiscUnits.NO_UNIT));

    ingredients.add(new RecipeIngredientHolder(
        "zucchini", 1d, MiscUnits.NO_UNIT));

    ingredients.add(new RecipeIngredientHolder(
        "frozen spinach", 100d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "diced tomatoes", 800d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        beyondBurgers, 4d, MiscUnits.NO_UNIT));

    ingredients.add(new RecipeIngredientHolder(
        "merlot", 500d, Volume.MILLILITRES));

    ingredients.add(new RecipeIngredientHolder(
        lasagneSheets, 1d, MiscUnits.NO_UNIT));

    ingredients.add(new RecipeIngredientHolder(
        "vegan cheese slices", 18d, MiscUnits.NO_UNIT));

    ingredients.add(new RecipeIngredientHolder(
        vegenaise, 100d, Mass.GRAMS));

    // Add the RecipeIngredients to the Recipe
    recipe.setRecipeIngredients(ingredients);

    // Return the recipe
    return recipe;

  }

  /**
   * Creates a curry RecipeHolder.
   *
   * @return RecipeHolder: The curry recipe.
   */
  private RecipeHolder createCurryRecipe() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    holder.setTitle("Tikka Masala Curry");
    holder.setDescription("Tired of hot curries? Try this bad boy; not too spicy, not too weak.");

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Prepare steam pot on hotplate.");
    steps.add("Dice the carrots and potatoes and add the and steam pot.");
    steps.add("Dice the tofu.");
    steps.add("Add tofu, bamboo shoots, water, and curry sauce to a large pot. Simmer on low temperature.");
    steps.add("Steam the broccoli in the microwave per packet directions.");
    steps.add("Prepare rice in rice cooker or stove and turn on.");
    steps.add("Add steamed potatoes, carrots, and broccoli to the curry pot when softened and simmer for 20 minutes, stirring frequently.");
    steps.add("Add coconut milk and simmer for a further 10 minutes on a very low temperature. Stir frequently.");
    steps.add("Enjoy!");

    // Add the steps to the recipe
    holder.setSteps(steps);

    // Create the RecipeIngredientsHolder object
    List<RecipeIngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "tofu";
    String curryPaste = "Patak's concentrated Tikka Masala curry paste";
    String coconutMilk = "coconut milk";

    // Add each ingredient to the list
    ingredients.add(new RecipeIngredientHolder(
        "rice (dry)", 5d, Volume.AU_CUPS));

    ingredients.add(new RecipeIngredientHolder(
        tofu, 454d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "frozen broccoli", 454d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "carrots", 800d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "potatoes", 800d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        "bamboo shoots", 225d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        curryPaste, 566d, Mass.GRAMS));

    ingredients.add(new RecipeIngredientHolder(
        coconutMilk, 600d, Volume.MILLILITRES));

    // Add the list to the RecipeHolder
    holder.setRecipeIngredients(ingredients);

    // Return the holder

    return holder;

  }

  public RecipeRepository getRepository() {
    return repository;
  }

}
