package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.MaterRepository;
import com.gerardbradshaw.mater.room.entities.Item;

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


  // - - - - - - - - - - - - - - - Getter methods - - - - - - - - - - - - - - -

  public LiveData<List<Item>> getLiveAllIngredients() {
    return repository.getLiveAllIngredients();
  }

  public void addIngredient(Item... items) {
    repository.addItem(items);
  }

  public void addIngredient(List<Item> items) {
    repository.addItem(items);
  }

  public Item getIngredient(int ingredientId) {
    return repository.getItem(ingredientId);
  }

}
