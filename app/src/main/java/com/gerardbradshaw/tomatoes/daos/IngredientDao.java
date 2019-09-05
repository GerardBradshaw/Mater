package com.gerardbradshaw.tomatoes.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gerardbradshaw.tomatoes.entities.Ingredient;

@Dao
public interface IngredientDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertIngredient(Ingredient ingredient);

  @Delete
  void deleteIngredient(Ingredient ingredient);

  @Update
  void updateIngredient(Ingredient ingredient);

  @Query("select * from Ingredient where name = :ingredientName limit 1")
  Ingredient getIngredient(String ingredientName);

  @Query("select ingredient_id from Ingredient where name = :ingredientName")
  int getIngredientId(String ingredientName);

  @Query("select * from Ingredient limit 1")
  Ingredient[] getAnyIngredient();

  @Query("select * from Ingredient order by name ASC")
  Ingredient[] getAllIngredients();

  @Query("select containsMilk from Ingredient where name = :ingredientName")
  Boolean getContainsMilk(String ingredientName);

  @Query("select containsEgg from Ingredient where name = :ingredientName")
  Boolean getContainsEgg(String ingredientName);

  @Query("select containsFish from Ingredient where name = :ingredientName")
  Boolean getContainsFish(String ingredientName);

  @Query("select containsCrustacean from Ingredient where name = :ingredientName")
  Boolean getContainsCrustacean(String ingredientName);

  @Query("select containsTreeNuts from Ingredient where name = :ingredientName")
  Boolean getContainsTreeNuts(String ingredientName);

  @Query("select containsPeanuts from Ingredient where name = :ingredientName")
  Boolean getContainsPeanuts(String ingredientName);

  @Query("select containsWheat from Ingredient where name = :ingredientName")
  Boolean getContainsWheat(String ingredientName);

  @Query("select containsSoy from Ingredient where name = :ingredientName")
  Boolean getContainsSoy(String ingredientName);

  @Query("select containsFodmap from Ingredient where name = :ingredientName")
  Boolean getContainsFodmap(String ingredientName);

}
