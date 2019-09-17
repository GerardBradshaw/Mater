package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Repo
  private RecipeRepository repository;

  // LiveData
  private LiveData<Integer> imageUpdateNotifier;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public ImageViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();

    // Set variables from repo
    imageUpdateNotifier = repository.bitmapUpdateNotifier();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  public LiveData<Integer> imageUpdateNotifier() {
    return imageUpdateNotifier;
  }

  public RecipeRepository getRepository() {
    return repository;
  }
}
