package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Repo
  private RecipeRepository repository;

  // LiveData
  private LiveData<List<RecipeSummary>> recipeSummaryList;
  private LiveData<Integer> observeImageUpdated;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeListViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();

    // Set variables from repo
    recipeSummaryList = repository.getAllRecipeSummaries();
    observeImageUpdated = repository.observeBitmapUpdated();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  public Bitmap getImage(Context context, String recipeId) {
    return repository.loadBitmap(context, recipeId);
  }

  public LiveData<Integer> observeImageUpdated() {
    return observeImageUpdated;
  }

  public RecipeRepository getRepository() {
    return repository;
  }
}
