package com.gerardbradshaw.tomatoes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private SharedPreferences sharedPreferences;
  private static final String sharedPrefFile = "com.gerardbradshaw.tomatoes";

  // Keys for data stored in shared prefs
  private String FIRST_LAUNCH = "launched";

  // Context
  private Context context;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * Constructor method.
   *
   * @param context: The activity context.
   */
  SharedPrefHelper(Context context) {

    // Initialize the SharedPreference
    sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

    // Set the context
    this.context = context;

  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  /**
   * Returns boolean for FIRST_LAUNCH, which is used to determine if the app has ever been launched.
   *
   * @return boolean: TRUE if the application has not been launched before, FALSE otherwise.
   */
  public boolean isFirstLaunch() {

    // Return the shared pref
    return sharedPreferences.getBoolean(FIRST_LAUNCH, true);

  }

  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  /**
   * Sets the state of FIRST_LAUNCH to false, which means the application has been launched > once.
   */
  public void setAsLaunched() {

    // Create an editor to take care of file operations
    SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

    // Save to the editor
    preferenceEditor.putBoolean(FIRST_LAUNCH, false);

    // Apply the edit
    preferenceEditor.apply();

  }
}
