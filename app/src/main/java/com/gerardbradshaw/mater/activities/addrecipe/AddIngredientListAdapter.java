package com.gerardbradshaw.mater.activities.addrecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.pojos.IngredientHolder;

import java.util.List;

public class AddIngredientListAdapter
    extends RecyclerView.Adapter<AddIngredientListAdapter.NewIngredientViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private List<IngredientHolder> ingredientHolders; // Cached copy
  private Context context;
  private static String LOG_TAG = "GGG - AddIngredientListAdapter";


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AddIngredientListAdapter(Context context) {
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
  public NewIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = inflater.inflate(R.layout.ingredient_input, parent, false);
    return new NewIngredientViewHolder(itemView, this);
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
  public void onBindViewHolder(@NonNull NewIngredientViewHolder viewHolder, int position) {
    if (ingredientHolders != null) {

      IngredientHolder holder = ingredientHolders.get(position);
      String name = holder.getName();
      double amount = holder.getAmount();
      String unit = holder.getUnit();

      if (!name.isEmpty()) {
        viewHolder.name.setText(name);

        if (!(Double.isNaN(amount) || amount == 0)) {
          viewHolder.amount.setText(Double.toString(amount));
        }
      }
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
    if(ingredientHolders != null) {
      return ingredientHolders.size();

    } else {
      return 0;
    }
  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  public void setData(List<IngredientHolder> ingredientHolders) {
    this.ingredientHolders = ingredientHolders;
    notifyDataSetChanged();
  }

  public IngredientHolder getIngredientHolderIdAtPosition(int position) {
    return ingredientHolders.get(position);
  }


  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  class NewIngredientViewHolder extends RecyclerView.ViewHolder {

    // Member variables
    private EditText name;
    private EditText amount;
    private Spinner units;
    final AddIngredientListAdapter adapter;

    // Constructor
    public NewIngredientViewHolder(@NonNull View itemView, AddIngredientListAdapter adapter) {
      super(itemView);

      // Initialize the views and adapter.
      name = itemView.findViewById(R.id.ingredientInput_nameInput);
      amount = itemView.findViewById(R.id.ingredientInput_amountInput);
      units = itemView.findViewById(R.id.ingredientInput_unitSpinner);
      this.adapter = adapter;
    }
  }
}