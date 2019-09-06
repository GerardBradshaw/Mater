package com.gerardbradshaw.tomatoes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RecipeRepository repository;
  private LiveData<List<RecipeSummary>> recipeSummaryList;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeViewModel(@NonNull Application application) {
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
