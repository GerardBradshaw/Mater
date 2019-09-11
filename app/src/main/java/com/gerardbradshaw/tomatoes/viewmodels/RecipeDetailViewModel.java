package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class RecipeDetailViewModel extends AndroidViewModel {


  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Repo
  private RecipeRepository repository;

  // LiveData
  private LiveData<String> title;
  private LiveData<String> description;
  private LiveData<RecipeIngredient> ingredients;
  private LiveData<RecipeStep> steps;

  // Other global variables



  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * VM constructor required to initialise functionality.
   *
   * @param application: The app.
   */
  public RecipeDetailViewModel(@NonNull Application application) {
    super(application);

    // Get a handle to the repository
    repository = new RecipeRepository(application);
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  /**
   * Gets the title of the specified recipe.
   *
   * @param recipeId, int: The ID of the recipe.
   * @return LiveData String of the title.
   */
  public LiveData<String> getTitle(int recipeId) {
    return repository.getTitle(recipeId);
  }

  /**
   * Gets the description of the specified recipe.
   *
   * @param recipeId, int: The ID of the recipe.
   * @return LiveData String of the description.
   */
  public LiveData<String> getDescription(int recipeId) {
    return repository.getDescription(recipeId);
  }

  public LiveData<RecipeIngredient> getIngredients(int recipeId) {
    return repository.getIngredients(recipeId);
  }

  public LiveData<RecipeStep> getSteps(int recipeId) {
    return repository.getRecipeSteps(recipeId);
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -






}
