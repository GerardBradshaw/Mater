package com.gerardbradshaw.tomatoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gerardbradshaw.tomatoes.viewholders.AddIngredientViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private Button addIngredientButton;
  private List<AddIngredientViewHolder> ingredientViewHolders;
  private EditText titleInput;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Get a handle on the views
    addIngredientButton = findViewById(R.id.addRecipeActivity_addIngredientButton);
    titleInput = findViewById(R.id.addRecipeActivity_titleInput);

    // Save the theme of the layout


    // Set listener for addIngredientButton
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredient();
      }
    });

    // Initialize ingredientViewHolders
    ingredientViewHolders = new ArrayList<>();

  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void addIngredient() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipeActivity_addIngredientLayout);

    // Inflate the view
    LinearLayout addIngredientView =
        (LinearLayout) inflater.inflate(R.layout.add_ingredient_layout, insertPoint, false);

    // Save the children of the View
    EditText nameInput = (EditText) addIngredientView.getChildAt(0);
    EditText amountInput = (EditText) addIngredientView.getChildAt(1);
    Spinner unitsSpinner = (Spinner) addIngredientView.getChildAt(2);

    // Save the new views to the list
    ingredientViewHolders.add(new AddIngredientViewHolder(nameInput, amountInput, unitsSpinner));

    // Get the index of the view
    int index = ingredientViewHolders.size() - 1;

    // Insert the view into main view
    insertPoint.addView(addIngredientView,index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

}
