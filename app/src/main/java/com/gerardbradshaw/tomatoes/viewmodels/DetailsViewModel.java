package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.room.TomatoesRepository;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.room.entities.Step;

public class DetailsViewModel extends AndroidViewModel {


  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TomatoesRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * VM constructor required to initialise functionality.
   *
   * @param application: The app.
   */
  public DetailsViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Methods - - - - - - - - - - - - - - -

  public LiveData<String> getLiveTitle(int recipeId) {
    return repository.getLiveTitle(recipeId);
  }

  public LiveData<String> getLiveDescription(int recipeId) {
    return repository.getLiveDescription(recipeId);
  }

  public LiveData<String> getLiveImageDirectory(int recipeId) {
    return repository.getLiveImageDirectory(recipeId);
  }

  public LiveData<RecipeIngredient[]> getLiveRecipeIngredients(int recipeId) {
    return repository.getLiveRecipeIngredients(recipeId);
  }

  public LiveData<Step[]> getLiveSteps(int recipeId) {
    return repository.getLiveSteps(recipeId);
  }

  public Ingredient getIngredient(int ingredientId) {
    return repository.getIngredient(ingredientId);
  }

  public TomatoesRepository getRepository() {
    return repository;
  }

  public void insertRecipeHolder(RecipeHolder recipeHolder) {
    repository.insertRecipeFromHolder(recipeHolder);
  }

}
