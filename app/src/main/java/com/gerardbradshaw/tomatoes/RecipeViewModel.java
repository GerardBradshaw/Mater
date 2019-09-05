package com.gerardbradshaw.tomatoes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class RecipeViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RecipeRepository recipeRepository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeViewModel(@NonNull Application application) {
    super(application);
  }

  // - - - - - - - - - - - - - - - ViewModel Methods - - - - - - - - - - - - - - -


}
