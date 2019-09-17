package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;

public class AddRecipeViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Repo
  private RecipeRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * VM constructor required to initialise functionality.
   *
   * @param application: The app.
   */
  public AddRecipeViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    TomatoesApplication tomatoesApplication = (TomatoesApplication) application;
    repository = tomatoesApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Repo wrapper methods - - - - - - - - - - - - - - -

  /**
   * Adds all recipe and ingredient information contained in a RecipeHolder.
   *
   * @param recipeHolder, RecipeHolder: The RecipeHolder to insert.
   */
  public void insertRecipeHolder(RecipeHolder recipeHolder) {
    repository.insertRecipeFromHolder(recipeHolder);
  }

  public void saveImage(String recipeTitle, Bitmap image) {
    repository.storeBitmap(recipeTitle, image);
  }


}
