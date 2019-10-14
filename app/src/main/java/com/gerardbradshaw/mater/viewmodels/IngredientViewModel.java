package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.MaterRepository;
import com.gerardbradshaw.mater.room.entities.Ingredient;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Get Ingredients - - - - - - - - - - - - - - -

  public List<Ingredient> getIngredients(int recipeId) {
    return repository.getIngredients(recipeId);
  }

  public List<Ingredient> getAllIngredients() {
    return repository.getAllIngredients();
  }

  public Ingredient getIngredient(int ingredientId) {
    return repository.getIngredient(ingredientId);
  }


  // - - - - - - - - - - - - - - - Add Ingredients - - - - - - - - - - - - - - -

  public void addIngredient(Ingredient... ingredients) {
    repository.addIngredient(ingredients);
  }

  public void addIngredient(List<Ingredient> ingredients) {
    repository.addIngredient(ingredients);
  }


  // - - - - - - - - - - - - - - - Update Ingredients - - - - - - - - - - - - - - -

  public void updateIngredient(Ingredient... ingredients) {
    repository.updateIngredient(ingredients);
  }

  public void updateIngredient(List<Ingredient> ingredients) {
    repository.updateIngredient(ingredients);
  }

  public void updateIngredientOnCurrentThread(List<Ingredient> ingredients) {
    repository.updateIngredientOnCurrentThread(ingredients);
  }

}
