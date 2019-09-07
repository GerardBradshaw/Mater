package com.gerardbradshaw.tomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddRecipeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Locate the ingredient layout
    LinearLayout ingredientLayout = findViewById(R.id.addRecipeActivity_addIngredientLayout);

    // Add the first field
    EditText nameInput = new EditText(this);

    // Define the parameters of the view
    LinearLayout.LayoutParams nameInputParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    // Set parameters to nameInput
    nameInput.setLayoutParams(nameInputParams);

    // Set other attributes
    nameInput.setHint(getString(R.string.addRecipeActivity_addStepLayout_stepInput_hint));


  }
}
