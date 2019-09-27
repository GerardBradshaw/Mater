package com.gerardbradshaw.mater.viewholders;

import android.widget.EditText;
import android.widget.Spinner;

public class IngredientInputViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private EditText nameInput;
  private EditText amountInput;
  private Spinner unitsSpinner;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientInputViewHolder(EditText nameInput, EditText amountInput, Spinner unitsSpinner) {
    this.nameInput = nameInput;
    this.amountInput = amountInput;
    this.unitsSpinner = unitsSpinner;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public EditText getNameInput() {
    return nameInput;
  }

  public EditText getAmountInput() {
    return amountInput;
  }

  public Spinner getUnitsSpinner() {
    return unitsSpinner;
  }
}
