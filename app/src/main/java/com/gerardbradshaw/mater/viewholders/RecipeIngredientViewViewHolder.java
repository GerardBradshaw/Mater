package com.gerardbradshaw.mater.viewholders;

import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class RecipeIngredientViewViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private CheckBox checkBox;
  private TextView quantityView;
  private TextView nameView;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeIngredientViewViewHolder(CheckBox checkBox, TextView quantityView, TextView nameView) {
    this.checkBox = checkBox;
    this.quantityView = quantityView;
    this.nameView = nameView;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public CheckBox getRadioButton() {
    return checkBox;
  }

  public TextView getQuantityView() {
    return quantityView;
  }

  public TextView getNameView() {
    return nameView;
  }

}
