package com.gerardbradshaw.tomatoes.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.tomatoes.helpers.TomatoesApplication;
import com.gerardbradshaw.tomatoes.room.TomatoesRepository;

import java.io.File;

public class ImageViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TomatoesRepository repository;
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


  // - - - - - - - - - - - - - - - Methods - - - - - - - - - - - - - - -

  public LiveData<Integer> imageUpdateNotifier() {
    return imageUpdateNotifier;
  }

  public void saveImage(String recipeTitle, Bitmap image) {
    repository.storeBitmap(recipeTitle, image);
  }

  public File getFile(String recipeTitle) {
    return repository.getFile(recipeTitle);
  }
}
