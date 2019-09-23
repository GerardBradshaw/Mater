package com.gerardbradshaw.mater.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private SharedPreferences sharedPreferences;
  private static final String sharedPrefFile = "com.gerardbradshaw.mater";
  private String FIRST_LAUNCH = "launched";

  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  /**
   * Constructor method.
   *
   * @param context: The activity context.
   */
  SharedPrefHelper(Context context) {
    sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
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
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(FIRST_LAUNCH, false);
    editor.apply();
  }

  public void putInt(String key, int number) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt(key, number);
    editor.apply();
  }

  public int getInt(String key, int defaultValue) {
    return sharedPreferences.getInt(key, defaultValue);
  }

  public void putString(String key, String string) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, string);
    editor.apply();
  }

  public String getString(String key, String defaultValue) {
    return sharedPreferences.getString(key, defaultValue);
  }

  public void putBoolean(String key, boolean bool) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(key, bool);
    editor.apply();
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return sharedPreferences.getBoolean(key, defaultValue);
  }

  public boolean contains(String key) {
    return sharedPreferences.contains(key);
  }
}
