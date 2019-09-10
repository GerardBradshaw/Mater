package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.pojos.RecipePojo;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Repo
  private RecipeRepository repository;

  // LiveData
  private LiveData<List<RecipeSummary>> recipeSummaryList;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * VM constructor required to initialise functionality.
   *
   * @param application: The app.
   */
  public RecipeViewModel(@NonNull Application application) {
    super(application);

    // Set variables from repo
    repository = new RecipeRepository(application);
    recipeSummaryList = repository.getAllRecipeSummaries();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  /**
   * Gets the Titles and Descriptions of all recipes in the database.
   *
   * @return LiveData List of RecipeSummary: the list of recipes
   */
  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  /**
   * Gets a RecipePojo for the specific recipe.
   *
   * @param recipeId, int: the ID of the recipe.
   * @return LiveData RecipePojo: the requested recipe.
   */
  public LiveData<RecipePojo> getRecipeHolder(int recipeId) {
    return null;
    // TODO create getRecipeHolder method with LiveData
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -

  /**
   * Adds all recipe and ingredient information contained in a RecipePojo.
   *
   * @param holder, RecipePojo: The RecipePojo to insert.
   */
  public void insertRecipeHolder(RecipePojo holder) {
    repository.insertRecipeFromHolder(holder);
  }




}
