package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.TomatoesRepository;
import com.gerardbradshaw.tomatoes.room.entities.Summary;

import java.util.List;

public class SummaryViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TomatoesRepository repository;

  private LiveData<List<Summary>> recipeSummaryList; // Cached copy


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public SummaryViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();

    // Set variables from repo
    recipeSummaryList = repository.getLiveAllSummaries();
  }


  // - - - - - - - - - - - - - - - Getter methods - - - - - - - - - - - - - - -

  public LiveData<List<Summary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  public TomatoesRepository getRepository() {
    return repository;
  }


  // - - - - - - - - - - - - - - - Other repo methods - - - - - - - - - - - - - - -

  public void deleteRecipe(int recipeId) {
    repository.deleteRecipe(recipeId);
  }
}
