package com.gerardbradshaw.tomatoes.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gerardbradshaw.tomatoes.room.daos.IngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.SummaryDao;
import com.gerardbradshaw.tomatoes.room.daos.RecipeIngredientDao;
import com.gerardbradshaw.tomatoes.room.daos.StepDao;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;

@Database(
    entities = {RecipeSummary.class, Ingredient.class, RecipeIngredient.class, RecipeStep.class},
    version = 1,
    exportSchema = false)
public abstract class TomatoesRoomDatabase extends RoomDatabase {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Define the DAOs that the database will use to interact with SQL
  public abstract SummaryDao recipeDao();

  public abstract IngredientDao ingredientDao();

  public abstract RecipeIngredientDao recipeIngredientDao();

  public abstract StepDao recipeStepDao();

  // Create an instance variable to ensure that the database is a singleton
  private static TomatoesRoomDatabase INSTANCE;


  // - - - - - - - - - - - - - - - DB methods - - - - - - - - - - - - - - -

  /**
   * Method used to instantiate the database. Ensures the DB is a singleton.
   *
   * @param context: The context of the MainActivity. Used to get the application context.
   * @return the TomatoesRoomDatabase object.
   */
  public static TomatoesRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {
      synchronized (TomatoesRoomDatabase.class) {
        if (INSTANCE == null) {

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
      // TODO database callback
    }
  };


  // - - - - - - - - - - - - - - - Populate DB AsyncTask Class - - - - - - - - - - - - - - -

  /**
   * Class used to recreate the database in an AsyncTask if it's empty.
   */
  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    // - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - -

    final SummaryDao summaryDao;
    final IngredientDao ingredientDao;
    final RecipeIngredientDao recipeIngredientDao;
    final StepDao stepDao;


    // - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - -

    /**
     * Constructor that populates the database in an AsyncTask.
     *
     * @param database: The instance of the database to issue the queries to via the DAOs.
     */
    private PopulateDbAsyncTask(TomatoesRoomDatabase database) {
      // Set the DAOs
      summaryDao = database.recipeDao();
      ingredientDao = database.ingredientDao();
      recipeIngredientDao = database.recipeIngredientDao();
      stepDao = database.recipeStepDao();
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
      return null;
      // TODO determine if needed!
    }
  }
}
