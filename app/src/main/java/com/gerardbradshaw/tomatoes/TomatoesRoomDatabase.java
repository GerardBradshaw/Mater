package com.gerardbradshaw.tomatoes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gerardbradshaw.tomatoes.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeDao;
import com.gerardbradshaw.tomatoes.entities.Ingredient;
import com.gerardbradshaw.tomatoes.entities.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Database(entities = {Recipe.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class TomatoesRoomDatabase extends RoomDatabase {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Define the DAOs that the database will use to interact with SQL
  public abstract IngredientDao ingredientDao();
  public abstract RecipeDao recipeDao();

  // Create an instance variable to ensure that the database is a singleton
  private static TomatoesRoomDatabase INSTANCE;


  // - - - - - - - - - - - - - - - DB methods - - - - - - - - - - - - - - -

  /**
   * Method used to instantiate the database. Ensures the DB is a singleton.
   * @param context: The context of the MainActivity. Used to get the application context.
   * @return the TomatoesRoomDatabase object.
   */
  public static TomatoesRoomDatabase getDatabase(final Context context) {

    if(INSTANCE == null) {
      synchronized (TomatoesRoomDatabase.class) {
        if(INSTANCE == null) {

          // Create the database
          INSTANCE = Room
              .databaseBuilder(context.getApplicationContext(),
                  TomatoesRoomDatabase.class, "tomatoes_database")
              .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating
              .addCallback(roomDatabaseCallback)
              .build();

        }
      }
    }

    return INSTANCE;
  }

  /**
   * Callback to delete all content and repopulate the database with default data.
   */
  private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      super.onOpen(db);

      // Start a new AsyncTask to populate the database if it's empty
      new PopulateDbAsyncTask(INSTANCE).execute();
    }
  };



  // - - - - - - - - - - - - - - - Populate DB AsyncTask Class - - - - - - - - - - - - - - -

  /**
   * Class used to recreate the database in an AsyncTask if it's empty.
   */
  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    // - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - -

    final IngredientDao ingredientDao;
    final RecipeDao recipeDao;


    // - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - -

    /**
     * Constructor that populates the database in an AsyncTask.
     *
     * @param database: The instance of the database to issue the queries to via the DAOs.
     */
    PopulateDbAsyncTask(TomatoesRoomDatabase database) {
      // Set the DAOs
      ingredientDao = database.ingredientDao();
      recipeDao = database.recipeDao();
    }


    // - - - - - - - - - - - - - - AsyncTask methods - - - - - - - - - - - - - -

    /**
     * Repopulates the database if it has no recipes. Process occurs on another thread.
     *
     * @param voids: void
     * @return Void: void
     */
    @Override
    protected Void doInBackground(Void... voids) {

      // If there are no recipes, add the lasagne recipe
      if (recipeDao.getAnyRecipe().length < 1) {

        // Create the lasagne recipe
        Recipe lasagneRecipe = createLasagneRecipe();

        // Add the recipe to the database using the DAO
        recipeDao.insertRecipe(lasagneRecipe);
      }

      return null;
    }


    // - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - -

    /**
     * Simple method used to create a Recipe object for lasagne.
     *
     * @return a Recipe object for a lasagne.
     */
    Recipe createLasagneRecipe() {

      // Create the lasagne recipe
      String title = "Beyond Lasagne";
      String description = "A delicious comfort food that will leave you thinking \"I CAN'T BELIEVE THIS IS VEGAN!";

      List<Ingredient> ingredients = new ArrayList<>();
      Map<Ingredient, Quantity> quantities = new HashMap<>();

      Ingredient sweetPotato = new Ingredient("Sweet potato");
      ingredients.add(sweetPotato);
      quantities.put(sweetPotato, new Quantity(800, Quantity.Unit.GRAMS));

      Ingredient capsicum = new Ingredient("Capsicum");
      ingredients.add(capsicum);
      quantities.put(capsicum, new Quantity(1, Quantity.Unit.NO_UNIT));

      Ingredient zucchini = new Ingredient("Zucchini");
      ingredients.add(zucchini);
      quantities.put(zucchini, new Quantity(1, Quantity.Unit.NO_UNIT));

      Ingredient frozenSpinach = new Ingredient("Frozen spinach");
      ingredients.add(frozenSpinach);
      quantities.put(frozenSpinach, new Quantity(100, Quantity.Unit.GRAMS));

      Ingredient dicedTomatoes = new Ingredient("Diced tomatoes");
      ingredients.add(dicedTomatoes);
      quantities.put(dicedTomatoes, new Quantity(800, Quantity.Unit.GRAMS));

      Ingredient beyondBurgers = new Ingredient("Beyond burgers");
      ingredients.add(beyondBurgers);
      quantities.put(beyondBurgers, new Quantity(4, Quantity.Unit.NO_UNIT));

      Ingredient merlot = new Ingredient("Merlot");
      ingredients.add(merlot);
      quantities.put(merlot, new Quantity(500, Quantity.Unit.MILLILITRES));

      Ingredient lasagneSheets = new Ingredient("Lasagne sheets");
      ingredients.add(lasagneSheets);
      quantities.put(lasagneSheets, new Quantity(1, Quantity.Unit.NO_UNIT));

      Ingredient cheese = new Ingredient("Vegan cheese slices");
      ingredients.add(cheese);
      quantities.put(cheese, new Quantity(18, Quantity.Unit.NO_UNIT));

      Ingredient vegenaise = new Ingredient("Vegenaise");
      ingredients.add(vegenaise);
      quantities.put(vegenaise, new Quantity(100, Quantity.Unit.GRAMS));

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

      return new Recipe(title, description);

    }

  }


}
