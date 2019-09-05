package com.gerardbradshaw.tomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gerardbradshaw.tomatoes.entities.RecipeSummary;
import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private ArrayList<RecipeSummary> recipeSummaryList; // Cached copy
  private RecipeClickedListener recipeClickedListener;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  RecipeListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }


  // - - - - - - - - - - - - - - - Adapter methods - - - - - - - - - - - - - - -

  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }



  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  public class RecipeViewHolder extends RecyclerView.ViewHolder {

    // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

    public final TextView recipeTitle;
    public final TextView recipeSummary;
    final RecipeListAdapter adapter;


    // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -
    public RecipeViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }



  // - - - - - - - - - - - - - - - RecipeClickedListener Interface - - - - - - - - - - - - - - -

  public interface RecipeClickedListener {
    void onRecipeClicked(RecipeSummary recipeSummary);
  }

}
