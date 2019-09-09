package com.gerardbradshaw.tomatoes.viewholders;

import android.widget.EditText;
import android.widget.TextView;

public class AddStepViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TextView number;
  private EditText step;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AddStepViewHolder(TextView number, EditText step) {
    this.number = number;
    this.step = step;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public TextView getNumber() {
    return number;
  }

  public EditText getStep() {
    return step;
  }
}
