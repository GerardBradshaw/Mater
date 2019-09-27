package com.gerardbradshaw.mater.activities.recipedetail;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.helpers.Units;
import com.gerardbradshaw.mater.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.mater.room.entities.RecipeIngredient;
import com.gerardbradshaw.mater.room.entities.Summary;

import java.util.List;

public class IngredientListAdapter
    extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private List<RecipeIngredientHolder> recipeIngredientHolders; // Cached copy
  private Context context;
  private static String LOG_TAG = "GGG - IngredientListAdapter";


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientListAdapter(Context context) {
    this.context = context;
    inflater = LayoutInflater.from(context);
  }


  // - - - - - - - - - - - - - - - Adapter methods - - - - - - - - - - - - - - -

  /**
   * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
   *
   * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
   * @param viewType the view type of the new View
   * @return RecipeViewHolder: The inflated view.
   */
  @NonNull
  @Override
  public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = inflater.inflate(R.layout.ingredient_detail, parent, false);
    return new IngredientViewHolder(itemView, this);
  }

  /**
   * Called by RecyclerView to display the data at the specified position and set up input
   * listeners. This method updates the contents of the itemView to reflect the item at the given
   * position.
   *
   * @param viewHolder the ViewHolder which should be updated to represent the contents of the item at the given position in the data set
   * @param position the position of the item within the adapter's data set
   */
  @Override
  public void onBindViewHolder(@NonNull IngredientViewHolder viewHolder, int position) {
    if (recipeIngredientHolders != null) {
      viewHolder.checkBox.setVisibility(View.VISIBLE);

      RecipeIngredientHolder holder = recipeIngredientHolders.get(position);
      String name = holder.getName();
      String quantity = Units.formatForDetailView(holder.getAmount(), holder.getUnit());

      viewHolder.name.setText(name);
      viewHolder.quantity.setText(quantity);

    } else {
      viewHolder.name.setText(context.getResources().getString(R.string.no_ingredients_message));
      viewHolder.checkBox.setVisibility(View.GONE);
    }

  }

  /**
   * Adapter method to return the number of items to be displayed in the RecyclerView.
   * This method is called many times, and when it is first called, this.summaryList has not
   * been updated. This means, initially, it will return zero.
   *
   * @return the number of items
   */
  @Override
  public int getItemCount() {
    if(recipeIngredientHolders != null) {
      return recipeIngredientHolders.size();

    } else {
      return 0;
    }
  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  public void setSummaryList(List<RecipeIngredientHolder> recipeIngredientHolders) {
    this.recipeIngredientHolders = recipeIngredientHolders;
    notifyDataSetChanged();
  }

  public RecipeIngredientHolder getRecipeIngredientHolderIdAtPosition(int position) {
    return recipeIngredientHolders.get(position);
  }


  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  class IngredientViewHolder extends RecyclerView.ViewHolder {

    // Member variables
    private final CheckBox checkBox;
    private final TextView quantity;
    private final TextView name;
    final IngredientListAdapter adapter;

    // Constructor
    public IngredientViewHolder(@NonNull View itemView, IngredientListAdapter adapter) {
      super(itemView);

      // Initialize the views and adapter.
      checkBox = itemView.findViewById(R.id.ingredientView_checkBox);
      quantity = itemView.findViewById(R.id.ingredientView_amount);
      name = itemView.findViewById(R.id.ingredientView_name);
      this.adapter = adapter;
    }
  }



}
