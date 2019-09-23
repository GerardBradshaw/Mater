package com.gerardbradshaw.mater.viewholders;

import android.widget.RadioButton;
import android.widget.TextView;

public class RecipeIngredientViewViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RadioButton radioButton;
  private TextView quantityView;
  private TextView nameView;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeIngredientViewViewHolder(RadioButton radioButton, TextView quantityView, TextView nameView) {
    this.radioButton = radioButton;
    this.quantityView = quantityView;
    this.nameView = nameView;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public RadioButton getRadioButton() {
    return radioButton;
  }

  public TextView getQuantityView() {
    return quantityView;
  }

  public TextView getNameView() {
    return nameView;
  }

}
