package com.gerardbradshaw.mater.viewholders;

import android.widget.TextView;

public class StepViewViewHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private TextView numberView;
  private TextView stepView;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public StepViewViewHolder(TextView numberView, TextView stepView) {
    this.numberView = numberView;
    this.stepView = stepView;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public TextView getNumberView() {
    return numberView;
  }

  public TextView getStepView() {
    return stepView;
  }


}
