package com.gerardbradshaw.mater.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.mater.room.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertItem(Item item);

  @Delete
  void deleteItem(Item... item);

  @Update
  void updateItem(Item item);

  // - - - - - - - - - - - - - - - LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from item_table order by name ASC")
  LiveData<List<Item>> getLiveAllItems();


  // - - - - - - - - - - - - - - - Non-LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from item_table where name = :ingredientName limit 1")
  Item getItem(String ingredientName);

  @Query("select * from item_table where item_id = :itemId")
  Item getItem(int itemId);

  @Query("select item_id from item_table where name = :ingredientName")
  int getItemId(String ingredientName);

  @Query("select * from item_table limit 1")
  Item getAnyItem();


  // Allergen queries

  @Query("select contains_milk from item_table where name = :ingredientName")
  String getContainsMilk(String ingredientName);

  @Query("select contains_egg from item_table where name = :ingredientName")
  String getContainsEgg(String ingredientName);

  @Query("select contains_fish from item_table where name = :ingredientName")
  String getContainsFish(String ingredientName);

  @Query("select contains_crustacean from item_table where name = :ingredientName")
  String getContainsCrustacean(String ingredientName);

  @Query("select contains_tree_nuts from item_table where name = :ingredientName")
  String getContainsTreeNuts(String ingredientName);

  @Query("select contains_peanuts from item_table where name = :ingredientName")
  String getContainsPeanuts(String ingredientName);

  @Query("select contains_wheat from item_table where name = :ingredientName")
  String getContainsWheat(String ingredientName);

  @Query("select contains_soy from item_table where name = :ingredientName")
  String getContainsSoy(String ingredientName);

  @Query("select contains_fodmap from item_table where name = :ingredientName")
  String getContainsFodmap(String ingredientName);

}
