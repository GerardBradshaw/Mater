package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.pojos.RecipeHolder;
import com.gerardbradshaw.tomatoes.room.RecipeRepository;

import java.util.List;

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

    // Set variables from repo
    repository = new RecipeRepository(application);
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
    repository.saveImage(recipeTitle, image);
  }


}
