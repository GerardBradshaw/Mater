package com.gerardbradshaw.mater.activities.recipedetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.main.RecipeListAdapter;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {
  @NonNull
  @Override
  public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }


  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  class IngredientViewHolder extends RecyclerView.ViewHolder {

    // Member variables
    // TODO views
    final IngredientListAdapter adapter;

    // Constructor
    public IngredientViewHolder(@NonNull View itemView, IngredientListAdapter adapter) {
      super(itemView);

      // Initialize the views and adapter.
      // TODO views
      this.adapter = adapter;
    }
  }



}
