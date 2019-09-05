package com.gerardbradshaw.tomatoes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.ArrayList;

public class RecipeViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RecipeRepository recipeRepository;
  private ArrayList<RecipeSummary> recipeSummaryList;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeViewModel(@NonNull Application application) {
    super(application);

    // Set variables from repo
    recipeRepository = new RecipeRepository(application);
    recipeSummaryList = recipeRepository.getAllRecipeSummaries();
  }


  // - - - - - - - - - - - - - - - Getter Methods - - - - - - - - - - - - - - -

  public ArrayList<RecipeSummary> getAllRecipeSummaries() {
    return recipeSummaryList;
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -

  // TODO add more functionality!


}
