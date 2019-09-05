package com.gerardbradshaw.tomatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gerardbradshaw.tomatoes.entities.RecipeSummary;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private List<RecipeSummary> recipeSummaryList; // Cached copy
  private RecipeClickedListener recipeClickedListener;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * Constructor for the adapter.
   *
   * @param context: The activity context.
   */
  RecipeListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }


  // - - - - - - - - - - - - - - - Adapter methods - - - - - - - - - - - - - - -

  /**
   * Adapter method which inflates an item view layout. Only called when a new ViewHolder is needed.
   *
   * @param parent:
   * @param viewType:
   * @return RecipeViewHolder: The inflated view.
   */
  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
    return new RecipeViewHolder(itemView, this);

  }

  /**
   * Adapter method used to bind data to a ViewHolder and set up an onClick listener.
   *
   * @param holder: The RecipeViewHolder to bind the data to.
   * @param position: The position of the holder in the adapter.
   */
  @Override
  public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

    if (recipeSummaryList != null) {
      // Retrieve the data for that position and add the data to the view
      RecipeSummary currentRecipeSummary = recipeSummaryList.get(position);
      holder.recipeTitleView.setText(currentRecipeSummary.getTitle());
      holder.recipeDescriptionView.setText(currentRecipeSummary.getDescription());

    } else {
      holder.recipeTitleView.setText("No recipes");
      holder.recipeDescriptionView.setText("Add a new recipe with the + button!");
    }

    // Set up listener for clicks
    // TODO set up listener for clicks

  }

  /**
   * Adapter method to return the number of items to be displayed in the RecyclerView.
   * This method is called many times, and when it is first called, this.recipeSummaryList has not
   * been updated. This means, initially, it will return zero.
   *
   * @return int: The number of items.
   */
  @Override
  public int getItemCount() {

    if(recipeSummaryList != null) {
      return recipeSummaryList.size();

    } else {
      return 0;
    }

  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  /**
   * Set the list of words to use in the RecyclerView.
   * @param recipeSummaryList: The list of recipe summaries.
   */
  void setRecipeSummaryList(List<RecipeSummary> recipeSummaryList) {
    this.recipeSummaryList = recipeSummaryList;
    notifyDataSetChanged();
  }

  /**
   * Returns the RecipeSummary at a given position in the adapter.
   * @param position, int: The position in the adapter.
   * @return RecipeSummary: The RecipeSummary at the given position.
   */
  public RecipeSummary getRecipeSummaryAtPosition(int position) {
    return recipeSummaryList.get(position);
  }

  /**
   * Sets the RecipeClickedListener on the view item.
   *
   * @param recipeClickedListener:
   */
  public void setRecipeClickedListener(RecipeClickedListener recipeClickedListener) {
    this.recipeClickedListener = recipeClickedListener;
  }



  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  class RecipeViewHolder extends RecyclerView.ViewHolder {

    // Member variables
    final TextView recipeTitleView;
    final TextView recipeDescriptionView;
    final RecipeListAdapter adapter;

    // Constructor
    RecipeViewHolder(@NonNull View itemView, RecipeListAdapter adapter) {
      super(itemView);

      // Initialize the views and adapter.
      recipeTitleView = itemView.findViewById(R.id.recipeListItem_title);
      recipeDescriptionView = itemView.findViewById(R.id.recipeListItem_summary);
      this.adapter = adapter;
    }
  }


  // - - - - - - - - - - - - - - - RecipeClickedListener Interface - - - - - - - - - - - - - - -

  public interface RecipeClickedListener {
    void onRecipeClicked(RecipeSummary recipeSummary);
  }

}
