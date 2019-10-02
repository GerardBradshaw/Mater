package com.gerardbradshaw.mater.activities.shoppinglist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.room.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter
    extends RecyclerView.Adapter<ItemListAdapter.IngredientViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private List<Item> itemList = new ArrayList<>();
  private static String LOG_TAG = "GGG - ItemListAdapter";
  private StockChangedListener stockChangedListener;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public ItemListAdapter(Context context) {
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

    View itemView = inflater.inflate(R.layout.shopping_list_item, parent, false);
    return new IngredientViewHolder(itemView, this);
  }

  /**
   * Called by RecyclerView to display the data at the specified position and set up input
   * listeners. This method updates the contents of the itemView to reflect the item at the given
   * position.
   *
   * @param holder the ViewHolder which should be updated to represent the contents of the item at the given position in the data set
   * @param position the position of the item within the adapter's data set
   */
  @Override
  public void onBindViewHolder(@NonNull final IngredientViewHolder holder, final int position) {

    Item currentItem = itemList.get(position);
    final int stockLevel = currentItem.getStockLevel();
    holder.textView.setText(currentItem.getName());

    if (stockLevel != 0) {
      holder.stockInput.setText(Integer.toString(stockLevel));
    } else {
      holder.stockInput.setText(null);
    }

    holder.stockInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        // Get the new input and save it to the current ingredient
        int stockLevel = 0;
        String input = editable.toString();

        if (!input.equals("")) {
          stockLevel = Integer.parseInt(input);
        }

        itemList.get(position).setStockLevel(stockLevel);

        if (stockChangedListener != null) {
          stockChangedListener.onStockLevelChanged(position, itemList.get(position));
        }

      }
    });

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
    if(itemList != null) {
      return itemList.size();
    } else {
      return 0;
    }
  }

  public void setData(List<Item> itemList) {
    this.itemList = itemList;
    notifyDataSetChanged();
  }

  public void setStockChangedListener(StockChangedListener stockChangedListener) {
    this.stockChangedListener = stockChangedListener;
  }


  // - - - - - - - - - - - - - - - ViewHolder - - - - - - - - - - - - - - -

  class IngredientViewHolder extends RecyclerView.ViewHolder {

    final EditText stockInput;
    final TextView textView;
    final ItemListAdapter adapter;

    public IngredientViewHolder(@NonNull View itemView, ItemListAdapter adapter) {
      super(itemView);

      // Initialize the views in the adapter
      stockInput = itemView.findViewById(R.id.shoppingListItem_stockInput);
      textView = itemView.findViewById(R.id.shoppingListItem_textView);
      this.adapter = adapter;
    }
  }

  public interface StockChangedListener {
    void onStockLevelChanged(int position, Item item);
  }
}
