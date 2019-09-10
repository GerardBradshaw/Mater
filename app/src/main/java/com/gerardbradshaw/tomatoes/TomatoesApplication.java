package com.gerardbradshaw.tomatoes;

import android.app.Application;

import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientPojo;
import com.gerardbradshaw.tomatoes.pojos.RecipePojo;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class TomatoesApplication extends Application {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private SharedPrefHelper sharedPrefHelper;
  private RecipeRepository repository;


  // - - - - - - - - - - - - - - - Application methods - - - - - - - - - - - - - - -

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize shared prefs
    sharedPrefHelper = new SharedPrefHelper(this);

    // Initialize the repository
    repository = new RecipeRepository(this);

    // Check if the application has been launched before. If not, create some recipes.
    if (sharedPrefHelper.isFirstLaunch()) {

      // Updated the firstLaunched status
      sharedPrefHelper.setAsLaunched();

      // Create the default recipes
      RecipePojo lasagneRecipePojo = createLasagneRecipe();
      RecipePojo curryRecipePojo = createCurryRecipe();

      // Add the recipes to the database
      repository.insertRecipeFromHolder(lasagneRecipePojo);
      repository.insertRecipeFromHolder(curryRecipePojo);

    }
  }

  /**
   * Creates a lasagne RecipePojo.
   *
   * @return RecipePojo: The lasagne recipe.
   */
  private RecipePojo createLasagneRecipe() {

    // Create a new RecipePojo object
    RecipePojo holder = new RecipePojo();

    // Set the title and description of the recipe
    holder.setTitle("Vegan Lasagne");
    holder.setDescription("A delicious comfort food that will leave you thinking \"I CAN'T BELIEVE THIS IS VEGAN!");

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
    holder.setSteps(steps);

    // Create a RecipeIngredientPojo object
    List<RecipeIngredientPojo> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String beyondBurgers = "Beyond burgers";
    String lasagneSheets = "lasagne sheets";
    String vegenaise = "Vegenaise";

    // Add each ingredient to the list
    ingredients.add(new RecipeIngredientPojo(
        "sweet potato", 800d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "sweet potato", 800d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "capsicum", 1d, RecipeIngredientPojo.Unit.NO_UNIT));

    ingredients.add(new RecipeIngredientPojo(
        "zucchini", 1d, RecipeIngredientPojo.Unit.NO_UNIT));

    ingredients.add(new RecipeIngredientPojo(
        "frozen spinach", 100d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "diced tomatoes", 800d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        beyondBurgers, 4d, RecipeIngredientPojo.Unit.NO_UNIT));

    ingredients.add(new RecipeIngredientPojo(
        "merlot", 500d, RecipeIngredientPojo.Unit.MILLILITRES));

    ingredients.add(new RecipeIngredientPojo(
        lasagneSheets, 1d, RecipeIngredientPojo.Unit.NO_UNIT));

    ingredients.add(new RecipeIngredientPojo(
        "vegan cheese slices", 18d, RecipeIngredientPojo.Unit.NO_UNIT));

    ingredients.add(new RecipeIngredientPojo(
        vegenaise, 100d, RecipeIngredientPojo.Unit.GRAMS));

    // Add the RecipeIngredients to the Recipe
    holder.setRecipeIngredients(ingredients);

    // Return the holder
    return holder;

  }

  /**
   * Creates a curry RecipePojo.
   *
   * @return RecipePojo: The curry recipe.
   */
  private RecipePojo createCurryRecipe() {

    // Create a new RecipePojo object
    RecipePojo holder = new RecipePojo();

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
    List<RecipeIngredientPojo> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "tofu";
    String curryPaste = "Patak's concentrated Tikka Masala curry paste";
    String coconutMilk = "coconut milk";

    // Add each ingredient to the list
    ingredients.add(new RecipeIngredientPojo(
        "rice (dry)", 5d, RecipeIngredientPojo.Unit.METRIC_CUPS));

    ingredients.add(new RecipeIngredientPojo(
        tofu, 454d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "frozen broccoli", 454d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "carrots", 800d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "potatoes", 800d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        "bamboo shoots", 225d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        curryPaste, 566d, RecipeIngredientPojo.Unit.GRAMS));

    ingredients.add(new RecipeIngredientPojo(
        coconutMilk, 600d, RecipeIngredientPojo.Unit.MILLILITRES));

    // Add the list to the RecipePojo
    holder.setRecipeIngredients(ingredients);

    // Return the holder

    return holder;

  }

}
