package com.gerardbradshaw.mater.helpers;

import android.app.Application;

import com.gerardbradshaw.mater.pojos.IngredientHolder;
import com.gerardbradshaw.mater.pojos.RecipeHolder;
import com.gerardbradshaw.mater.room.MaterRepository;
import com.gerardbradshaw.mater.helpers.Units.Mass;
import com.gerardbradshaw.mater.helpers.Units.Volume;
import com.gerardbradshaw.mater.helpers.Units.MiscUnits;

import java.util.ArrayList;
import java.util.List;

public class MaterApplication extends Application {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;

  private AsyncTaskScheduler taskScheduler = new AsyncTaskScheduler();


  // - - - - - - - - - - - - - - - Application methods - - - - - - - - - - - - - - -

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize the repository
    repository = new MaterRepository(this);

    // Initialize shared prefs
    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);

    // Check if the application has been launched before. If not, create some recipes.
    if (sharedPrefHelper.isFirstLaunch()) {

      // Updated the firstLaunched status
      sharedPrefHelper.setAsLaunched();

      // Create the default recipes
      RecipeHolder lasagneRecipeHolder = createLasagneRecipeHolder();
      RecipeHolder curryRecipeHolder = createCurryRecipeHolder();
      RecipeHolder satayRecipeHolder = createSatayRecipeHolder();

      // Add the recipes to the database
      repository.insertRecipeFromHolder(satayRecipeHolder);
      repository.insertRecipeFromHolder(curryRecipeHolder);
      repository.insertRecipeFromHolder(lasagneRecipeHolder);

      // Store the lasagne image
      int lasagneImageId = this.getResources().getIdentifier(
          "img_lasagne", "drawable", getPackageName());
      repository.storeBitmap(lasagneRecipeHolder.getTitle(), lasagneImageId);

      // Store the curry image
      int curryImageId = this.getResources().getIdentifier(
          "img_curry", "drawable", getPackageName());
      repository.storeBitmap(curryRecipeHolder.getTitle(), curryImageId);

    }
  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  private RecipeHolder createLasagneRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    // Set the title and description of the holder
    holder.setTitle("Vegan Lasagne");
    holder.setDescription("A delicious comfort food that will leave you thinking \"I can't believe I just ate a whole lasagne\".");
    holder.setImageDirectory("Default image");
    holder.setServings(8);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Dice the sweet potato, zucchini, and capsicum into small cubes.");
    steps.add("Sauté diced vegetables in large fry pan on medium-high temperature for 10 minutes or until sweet potato has softened.");
    steps.add("Add diced mater and Beyond burgers to pan. Break up the burger patties and combine.");
    steps.add("Add red wine and set pan to simmer, stirring occasionally.");
    steps.add("While vegetables are simmering, line baking dish with lasagne sheets and place Vegenaise into a snap-lock bag.");
    steps.add("When most of the juice has boiled away (some juice is desirable to properly soften the lasagne sheets), use a spoon to spread a thin layer of the mix over the lasagne sheets.");
    steps.add("Cover the mix with cheese. Add another 2 layers of lasagne sheets, vegetable mix, and cheese as before.");
    steps.add("Make a small cut in the corner of the snap-lock bag and squeeze (pipe) the vegenaise over the top layer of cheese.");
    steps.add("Bake for ~30 minutes at 215°C (420°F) or until golden brown on top.");
    steps.add("Allow lasagne to cool for 5-10 minutes and slice into desired portion sizes.");
    steps.add("Enjoy!");

    // Add the steps to the holder
    holder.setSteps(steps);

    // Create a IngredientHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String beyondBurgers = "Beyond burgers";
    String lasagneSheets = "lasagne sheets";
    String vegenaise = "Vegenaise";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        "sweet potato", 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "capsicum", 1d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "zucchini", 1d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "frozen spinach", 100d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "diced mater", 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        beyondBurgers, 4d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "merlot", 500d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        lasagneSheets, 1d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "vegan cheese slices", 18d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        vegenaise, 100d, Mass.GRAMS));

    // Add the Ingredients to the Recipe
    holder.setIngredientHolders(ingredients);

    // Return the holder
    return holder;

  }

  private RecipeHolder createCurryRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    holder.setTitle("Tikka Masala Curry");
    holder.setDescription("Tired of hot curries? Try this bad boy; not too spicy, not too weak.");
    holder.setImageDirectory("Default image");
    holder.setServings(20);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Prepare steam pot on hotplate.");
    steps.add("Dice the carrots and potatoes and add them to the steam pot.");
    steps.add("Dice the tofu.");
    steps.add("Add tofu, bamboo shoots, water, and curry sauce to a large pot. Simmer on low temperature.");
    steps.add("Steam the broccoli in the microwave per packet directions.");
    steps.add("Prepare rice in rice cooker or stove and turn on.");
    steps.add("Add steamed potatoes, carrots, and broccoli to the curry pot when softened and simmer for 20 minutes, stirring frequently.");
    steps.add("Add coconut milk and simmer for a further 10 minutes on a very low temperature. Stir frequently.");
    steps.add("Enjoy!");

    // Add the steps to the recipe
    holder.setSteps(steps);

    // Create the IngredientsHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "firm tofu";
    String curryPaste = "Patak's concentrated Tikka Masala curry paste";
    String coconutMilk = "coconut milk";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        "rice (dry)", 5d, Volume.AU_CUPS));

    ingredients.add(new IngredientHolder(
        tofu, 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "frozen broccoli", 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "carrots", 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "potatoes", 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "bamboo shoots", 225d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        curryPaste, 566d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        coconutMilk, 600d, Volume.MILLILITRES));

    // Add the list to the RecipeHolder
    holder.setIngredientHolders(ingredients);

    // Return the holder

    return holder;

  }

  private RecipeHolder createSatayRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    holder.setTitle("Tofu Satay");
    holder.setDescription("Smooth, nutty, and just the right amount of fantastic.");
    holder.setImageDirectory("Default image");
    holder.setServings(12);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Dice tofu into cubes and fry in saucepan on low temperature. Turn and cook without oil until golden brown.");
    steps.add("Wash and slice bok-choy into 1/2 inch pieces. Set aside.");
    steps.add("Boil water in a medium pot and add cook pasta per packet directions. Set aside once finished.");
    steps.add("Set tofu aside and saute capsicum and broccoli in the saucepan.");
    steps.add("Add bok-choy to saucepan during final 5 minutes of saute. All vegetables should be hot and crispy.");
    steps.add("For the sauce, add peanut butter, soy sauce, sesame oil, lime juice, and water to a microwave safe pourer and microwave on high for 3 minutes.");
    steps.add("Blend sauce with electric mixer until it makes a smooth paste.");
    steps.add("Add pasta, vegetables, tofu, and sauce to a bowl and serve.");
    steps.add("Enjoy!");

    // Add the steps to the recipe
    holder.setSteps(steps);

    // Create the IngredientHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "firm tofu";
    String pasta = "pasta";
    String peanutButter = "peanut butter";
    String soySauce = "soy sauce";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        tofu, 600d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "capsicum", 1d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "frozen broccoli", 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "bok-choy", 2d, MiscUnits.NO_UNIT));

    ingredients.add(new IngredientHolder(
        pasta, 320d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        peanutButter, 285d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        soySauce, 90d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "sesame oil", 60d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "lime juice", 120d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "water", 180d, Volume.MILLILITRES));

    // Add the list to the RecipeHolder
    holder.setIngredientHolders(ingredients);

    // Return the holder

    return holder;

  }

  public MaterRepository getRepository() {
    return repository;
  }

  public AsyncTaskScheduler getTaskScheduler() {
    return taskScheduler;
  }

}
