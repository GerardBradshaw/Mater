package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.TomatoesRepository;
import com.gerardbradshaw.tomatoes.room.entities.Ingredient;

public class IngredientViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TomatoesRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Getter methods - - - - - - - - - - - - - - -

  public Ingredient[] getAllIngredients() {
    return null; //repository.getAllIngredients();
  }

}
