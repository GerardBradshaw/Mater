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


  // - - - - - - - - - - - - - - - Get Items - - - - - - - - - - - - - - -

  public List<Item> getAllItems() {
    return repository.getAllItems();
  }

  public Item getItem(int itemId) {
    return repository.getItem(itemId);
  }


  // - - - - - - - - - - - - - - - Add Items - - - - - - - - - - - - - - -

  public void addItem(Item... items) {
    repository.addItem(items);
  }

  public void addItem(List<Item> items) {
    repository.addItem(items);
  }


  // - - - - - - - - - - - - - - - Update Items - - - - - - - - - - - - - - -

  public void updateItem(Item... items) {
    repository.updateItem(items);
  }

  public void updateItem(List<Item> items) {
    repository.updateItem(items);
  }

}
