package com.gerardbradshaw.mater.activities.addrecipe;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
  private static String LOG_TAG = "GGG - AddIngredientListAdapter";

  private NameEditedListener nameEditedListener;
  private AmountEditedListener amountEditedListener;

  private Context context;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AddIngredientListAdapter(Context context) {
    inflater = LayoutInflater.from(context);
    this.context = context;
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
    View itemView = inflater.inflate(R.layout.ingredient_add, parent, false);
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
  public void onBindViewHolder(@NonNull final NewIngredientViewHolder viewHolder, int position) {
    if (ingredientHolders != null) {

      IngredientHolder holder = ingredientHolders.get(position);
      final String name = holder.getName();
      double amount = holder.getAmount();
      String unit = holder.getUnit();
      String category = holder.getCategory();

      // Set up the units Spinner and the drop down appearance
      ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
          context, R.array.global_units_array, android.R.layout.simple_spinner_item);
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      // Set up listener for unit changes
      viewHolder.units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
      });

      // Set the adapter to the spinner
      viewHolder.units.setAdapter(spinnerAdapter);

      // TODO Set up category Spinner and the drop down appearance

      // Set the name, amount, unit and category if a name exists, otherwise make them empty
      // TODO set default unit and category
      if (!name.isEmpty()) {
        viewHolder.name.setText(name);

        if (!(Double.isNaN(amount) || amount == 0)) {
          viewHolder.amount.setText(Double.toString(amount));
        }

      } else {
        viewHolder.name.setText(null);
        viewHolder.amount.setText(null);
      }

      // Set up listener for name changes
      viewHolder.name.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
          if (nameEditedListener != null) {
            nameEditedListener.onNameEdited(viewHolder.getAdapterPosition(), editable.toString());
          }
        }
      });

      // Set up listener for amount changes
      viewHolder.amount.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
          if (amountEditedListener != null) {
            double amount = 0;
            if (!editable.toString().isEmpty()) {
              amount = Double.parseDouble(editable.toString());
            }
            amountEditedListener.onAmountEdited(viewHolder.getAdapterPosition(), amount);
          }
        }
      });

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

  public void setNameEditedListener(NameEditedListener nameEditedListener) {
    this.nameEditedListener = nameEditedListener;
  }

  public void setAmountEditedListener(AmountEditedListener amountEditedListener) {
    this.amountEditedListener = amountEditedListener;
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


  // - - - - - - - - - - - - - - - IngredientModifiedListener Interface - - - - - - - - - - - - - - -

  public interface NameEditedListener {
    void onNameEdited(int position, String newName);
  }

  public interface AmountEditedListener {
    void onAmountEdited(int position, double amount);
  }

}