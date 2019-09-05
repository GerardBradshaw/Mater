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
import com.gerardbradshaw.tomatoes.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.daos.RecipeStepDao;
import com.gerardbradshaw.tomatoes.entities.Ingredient;
import com.gerardbradshaw.tomatoes.entities.Recipe;
import com.gerardbradshaw.tomatoes.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.entities.RecipeStep;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Recipe.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class TomatoesRoomDatabase extends RoomDatabase {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Define the DAOs that the database will use to interact with SQL
  public abstract RecipeDao recipeDao();
  public abstract IngredientDao ingredientDao();
  public abstract RecipeIngredientDao recipeIngredientDao();
  public abstract RecipeStepDao recipeStepDao();

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

    final RecipeDao recipeDao;
    final IngredientDao ingredientDao;
    final RecipeIngredientDao recipeIngredientDao;
    final RecipeStepDao recipeStepDao;


    // - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - -

    /**
     * Constructor that populates the database in an AsyncTask.
     *
     * @param database: The instance of the database to issue the queries to via the DAOs.
     */
    PopulateDbAsyncTask(TomatoesRoomDatabase database) {
      // Set the DAOs
      recipeDao = database.recipeDao();
      ingredientDao = database.ingredientDao();
      recipeIngredientDao = database.recipeIngredientDao();
      recipeStepDao = database.recipeStepDao();
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

        // Define the title and description of a Lasagne recipe
        String title = "Beyond Lasagne";
        String description = "A delicious comfort food that will leave you thinking \"I CAN'T BELIEVE THIS IS VEGAN!";

        // Add a new Lasagne recipe to the DAO
        recipeDao.insertRecipe(new Recipe(title, description));

        // Retrieve the unique ID of the lasagne recipe
        int lasagneRecipeId = recipeDao.getRecipeId(title);

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

        // Add the steps to the database using the lasagne ID
        for(int i = 0; i < steps.size(); i++) {
          int stepNumber = i + 1;
          RecipeStep recipeStep = new RecipeStep(lasagneRecipeId, stepNumber, steps.get(i));
          recipeStepDao.insertRecipeStep(recipeStep);
        }

        // Create an arrayList to store the ingredient name
        List<Object[]> ingredientsList = new ArrayList<>();

        // Define the names of ingredients that contain allergens
        String beyondBurgers = "Beyond burgers";
        String lasagneSheets = "lasagne sheets";
        String vegenaise = "Vegenaise";

        // Add each ingredient name, amount and unit to the ingredient list in the form
        // {name (String), amount (double), unit (String)}
        ingredientsList.add(new Object[] {"sweet potato", 800, RecipeIngredient.Unit.GRAMS.name()});
        ingredientsList.add(new Object[] {"capsicum", 1, RecipeIngredient.Unit.NO_UNIT.name()});
        ingredientsList.add(new Object[] {"zucchini", 1, RecipeIngredient.Unit.NO_UNIT.name()});
        ingredientsList.add(new Object[] {"frozen spinach", 100, RecipeIngredient.Unit.GRAMS.name()});
        ingredientsList.add(new Object[] {"diced tomatoes", 800, RecipeIngredient.Unit.GRAMS.name()});
        ingredientsList.add(new Object[] {beyondBurgers, 4, RecipeIngredient.Unit.NO_UNIT.name()});
        ingredientsList.add(new Object[] {"merlot", 500, RecipeIngredient.Unit.MILLILITRES.name()});
        ingredientsList.add(new Object[] {lasagneSheets, 1, RecipeIngredient.Unit.NO_UNIT.name()});
        ingredientsList.add(new Object[] {"vegan cheese slices", 18, RecipeIngredient.Unit.NO_UNIT.name()});
        ingredientsList.add(new Object[] {vegenaise, 100, RecipeIngredient.Unit.GRAMS.name()});

        // Create ingredients from each
        for(int i = 0; i < ingredientsList.size(); i++) {

          // Get the name, amount, and units of the ingredient from the array
          String name = (String) ingredientsList.get(i)[0];
          double amount = (double) ingredientsList.get(i)[1];
          String units = (String) ingredientsList.get(i)[2];

          // Create an ingredient from the name
          Ingredient ingredient = new Ingredient(name);

          // Add the Ingredient to the DAO
          ingredientDao.insertIngredient(ingredient);

          // Get the ID of the ingredient from the DAO
          int ingredientId = ingredientDao.getIngredientId(name);

          // Create a RecipeIngredient using this ID along with the Recipe ID, amount, and units
          RecipeIngredient recipeIngredient =
              new RecipeIngredient(lasagneRecipeId, ingredientId, amount, units);

          // Add the RecipeIngredient to the DAO
          recipeIngredientDao.insertRecipeIngredient(recipeIngredient);

        }

        // Update Beyond burger Ingredient because it contains allergens
        Ingredient beyondBurgerIngredient = ingredientDao.getIngredient(beyondBurgers);
        beyondBurgerIngredient.setContainsSoy(Ingredient.ContainsAllergen.CONTAINS_AS_INGREDIENT.name());
        ingredientDao.updateIngredient(beyondBurgerIngredient);

        // Update lasagne sheets because it contains allergens
        Ingredient lasagneSheetsIngredient = ingredientDao.getIngredient(lasagneSheets);
        lasagneSheetsIngredient.setContainsWheat(Ingredient.ContainsAllergen.CONTAINS_AS_INGREDIENT.name());
        ingredientDao.updateIngredient(lasagneSheetsIngredient);

        // Update vegenaise because it contains allergens
        Ingredient vegenaiseIngredient = ingredientDao.getIngredient(vegenaise);
        vegenaiseIngredient.setContainsSoy(Ingredient.ContainsAllergen.CONTAINS_AS_INGREDIENT.name());
        ingredientDao.updateIngredient(vegenaiseIngredient);

        }

      return null;
    }
    
  }

}
