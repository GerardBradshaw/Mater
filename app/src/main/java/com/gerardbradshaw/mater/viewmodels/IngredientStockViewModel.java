package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.pojos.StockHolder;
import com.gerardbradshaw.mater.room.MaterRepository;

import java.util.List;

public class IngredientStockViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientStockViewModel(Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public List<StockHolder> getStockLevels() {
    return repository.getStockLevels();
  }

  public List<StockHolder> getStockLevels(int recipeId) {
    return repository.getStockLevels(recipeId);
  }


}
