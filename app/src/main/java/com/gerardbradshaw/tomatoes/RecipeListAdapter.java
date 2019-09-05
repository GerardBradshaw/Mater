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
import java.util.List;

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

    View itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
    return new RecipeViewHolder(itemView, this);

  }

  @Override
  public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

    if (recipeSummaryList != null) {
      // Retrieve the data for that position and add the data to the view
      RecipeSummary currentRecipeSummary = recipeSummaryList.get(position);
      holder.recipeTitleView.setText(currentRecipeSummary.getTitle());
      holder.recipeSummaryView.setText(currentRecipeSummary.getDescription());

    } else {
      holder.recipeTitleView.setText("No recipes");
      holder.recipeSummaryView.setText("Add a new recipe with the + button!");
    }

    // Set up listener for clicks
    // TODO set up listener for clicks

  }

  @Override
  public int getItemCount() {

    if(recipeSummaryList != null) {
      return recipeSummaryList.size();

    } else {
      return 0;
    }

  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  void setRecipeSummaryList(ArrayList<RecipeSummary> recipeSummaryList) {
    this.recipeSummaryList = recipeSummaryList;
  }

  public RecipeSummary getRecipeSummaryAtPosition(int position) {
    return recipeSummaryList.get(position);
  }

  public void setRecipeClickedListener(RecipeClickedListener recipeClickedListener) {
    this.recipeClickedListener = recipeClickedListener;
  }



  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  public class RecipeViewHolder extends RecyclerView.ViewHolder {

    // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

    public final TextView recipeTitleView;
    public final TextView recipeSummaryView;
    final RecipeListAdapter adapter;


    // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

    public RecipeViewHolder(@NonNull View itemView, RecipeListAdapter adapter) {
      super(itemView);

      // Initialize the views
      recipeTitleView = itemView.findViewById(R.id.recipeListItem_title);
      recipeSummaryView = itemView.findViewById(R.id.recipeListItem_summary);

      // Initialize the adapter
      this.adapter = adapter;
    }
  }



  // - - - - - - - - - - - - - - - RecipeClickedListener Interface - - - - - - - - - - - - - - -

  public interface RecipeClickedListener {
    void onRecipeClicked(RecipeSummary recipeSummary);
  }

}
