package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.room.RecipeRepository;
import com.gerardbradshaw.tomatoes.room.entities.RecipeSummary;

import java.util.List;

public class RecipeSummaryViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RecipeRepository repository;
  private LiveData<List<RecipeSummary>> recipeSummaryList;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeSummaryViewModel(@NonNull Application application) {
    super(application);

    // Set variables from repo
    repository = new RecipeRepository(application);
    recipeSummaryList = repository.getAllRecipeSummaries();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  public LiveData<List<RecipeSummary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -

  // TODO add more functionality!


}
