package com.gerardbradshaw.mater.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gerardbradshaw.mater.helpers.MaterApplication;
import com.gerardbradshaw.mater.room.MaterRepository;
import com.gerardbradshaw.mater.room.entities.Item;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public ItemViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();
  }


  // - - - - - - - - - - - - - - - Getter methods - - - - - - - - - - - - - - -

  public LiveData<List<Item>> getLiveAllItems() {
    return repository.getLiveAllItems();
  }

  public List<Item> getAllItems() {
    return repository.getAllItems();
  }

  public void addItem(Item... items) {
    repository.addItem(items);
  }

  public void addItem(List<Item> items) {
    repository.addItem(items);
  }

  public Item getItem(int itemId) {
    return repository.getItem(itemId);
  }

}
