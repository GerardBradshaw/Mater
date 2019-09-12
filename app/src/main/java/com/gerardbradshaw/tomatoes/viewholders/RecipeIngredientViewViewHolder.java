package com.gerardbradshaw.tomatoes.viewholders;

import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class RecipeIngredientViewViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RadioButton radioButton;
  private TextView textView;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeIngredientViewViewHolder(RadioButton radioButton, TextView textView) {
    this.radioButton = radioButton;
    this.textView = textView;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public RadioButton getRadioButton() {
    return radioButton;
  }

  public TextView getTextView() {
    return textView;
  }

}
