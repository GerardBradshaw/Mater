package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeStep;

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

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  public LiveData<String> getTitle(int recipeId) {
    return repository.getRecipeTitle(recipeId);
  }

  public LiveData<String> getDescription(int recipeId) {
    return repository.getRecipeDescription(recipeId);
  }

  public LiveData<String> getImageDirectory(int recipeId) {
    return repository.getRecipeImageDirectory(recipeId);
  }

  public LiveData<RecipeIngredient[]> getIngredients(int recipeId) {
    return repository.getRecipeIngredients(recipeId);
  }

  public LiveData<RecipeStep[]> getSteps(int recipeId) {
    return repository.getRecipeSteps(recipeId);
  }

  public Ingredient getIngredient(int ingredientId) {
    return repository.getIngredient(ingredientId);
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -






}
