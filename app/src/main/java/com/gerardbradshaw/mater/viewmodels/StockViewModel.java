package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.pojos.StockHolder;
import com.gerardbradshaw.mater.room.MaterRepository;

import java.util.List;

public class StockViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public StockViewModel(Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  


}
