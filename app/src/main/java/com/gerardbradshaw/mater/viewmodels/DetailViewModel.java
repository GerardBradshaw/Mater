package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.mater.util.MaterApplication;
import com.gerardbradshaw.mater.models.RecipeHolder;
import com.gerardbradshaw.mater.room.MaterRepository;
import com.gerardbradshaw.mater.room.entities.Ingredient;
import com.gerardbradshaw.mater.room.entities.Step;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {


  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * VM constructor required to initialise functionality.
   *
   * @param application: The app.
   */
  public DetailViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Methods - - - - - - - - - - - - - - -

  public LiveData<String> getLiveTitle(int recipeId) {
    return repository.getLiveTitle(recipeId);
  }

  public LiveData<String> getLiveDescription(int recipeId) {
    return repository.getLiveDescription(recipeId);
  }

  public LiveData<Integer> getLiveServings(int recipeId) {
    return repository.getLiveServings(recipeId);
  }

  public LiveData<String> getLiveImageDirectory(int recipeId) {
    return repository.getLiveImageDirectory(recipeId);
  }

  public LiveData<List<Ingredient>> getLiveIngredients(int recipeId) {
    return repository.getLiveIngredients(recipeId);
  }

  public LiveData<Step[]> getLiveSteps(int recipeId) {
    return repository.getLiveSteps(recipeId);
  }

  public void insertRecipeHolder(RecipeHolder recipeHolder) {
    repository.insertRecipeFromHolder(recipeHolder);
  }

  public RecipeHolder getRecipeHolder(int recipeId) {
    return repository.getRecipeHolder(recipeId);
  }

}
