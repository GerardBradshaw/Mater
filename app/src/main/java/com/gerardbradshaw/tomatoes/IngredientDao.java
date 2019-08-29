package com.gerardbradshaw.tomatoes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IngredientDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertIngredient(Ingredient ingredient);

  @Delete
  void deleteIngredient(Ingredient ingredient);

  @Update
  void updateIngredient(Ingredient ingredient);

  @Query("select * from ingredients_table limit 1")
  Ingredient[] getAnyIngredient();

  @Query("select * from ingredients_table order by ASC")
  Ingredient[] getAllIngredients();

  @Query("select containsMilk from ingredients_table where name = :ingredientName")
  Boolean getContainsMilk(String ingredientName);

  @Query("select containsEgg from ingredients_table where name = :ingredientName")
  Boolean getContainsEgg(String ingredientName);

  @Query("select containsFish from ingredients_table where name = :ingredientName")
  Boolean getContainsFish(String ingredientName);

  @Query("select containsCrustacean from ingredients_table where name = :ingredientName")
  Boolean getContainsCrustacean(String ingredientName);

  @Query("select containsTreeNuts from ingredients_table where name = :ingredientName")
  Boolean getContainsTreeNuts(String ingredientName);

  @Query("select containsPeanuts from ingredients_table where name = :ingredientName")
  Boolean getContainsPeanuts(String ingredientName);

  @Query("select containsWheat from ingredients_table where name = :ingredientName")
  Boolean getContainsWheat(String ingredientName);

  @Query("select containsSoy from ingredients_table where name = :ingredientName")
  Boolean getContainsSoy(String ingredientName);

  @Query("select containsFodmap from ingredients_table where name = :ingredientName")
  Boolean getContainsFodmap(String ingredientName);

}
