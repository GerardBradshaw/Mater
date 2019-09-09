package com.gerardbradshaw.tomatoes.viewholders;

import android.widget.EditText;
import android.widget.Spinner;

public class AddIngredientViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private EditText nameInput;
  private EditText amountInput;
  private Spinner unitsSpinner;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AddIngredientViewHolder(EditText nameInput, EditText amountInput, Spinner unitsSpinner) {
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
