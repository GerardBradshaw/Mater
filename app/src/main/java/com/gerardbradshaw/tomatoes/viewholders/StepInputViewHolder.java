package com.gerardbradshaw.tomatoes.viewholders;

import android.widget.EditText;
import android.widget.TextView;

public class StepInputViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TextView number;
  private EditText step;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public StepInputViewHolder(TextView number, EditText step) {
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
