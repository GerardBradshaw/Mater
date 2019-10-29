package com.gerardbradshaw.mater.activities.main;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.addrecipe.AddRecipeActivity;
import com.gerardbradshaw.mater.activities.recipedetail.RecipeDetailActivity;
import com.gerardbradshaw.mater.activities.settings.SettingsActivity;
import com.gerardbradshaw.mater.activities.shoppinglist.ShoppingListActivity;
import com.gerardbradshaw.mater.room.entities.Summary;
import com.gerardbradshaw.mater.viewmodels.ImageViewModel;
import com.gerardbradshaw.mater.viewmodels.SummaryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private RecipeListAdapter recipeListAdapter;
  private SummaryViewModel summaryViewModel;
  private ImageViewModel imageViewModel;
  private static String LOG_TAG = "GGG - Main Activity";

  static final String ALARM_NOTIF_CHANNEL_ID = "com.gerardbradshaw.mater.ALARM_NOTIF_CHANNEL_ID";
  static final int ALARM_NOTIF_ID = 1;

  private static final String packageName = "com.gerardbradshaw.mater";
  public static final String EXTRA_RECIPE_ID = packageName + ".EXTRA_RECIPE_ID";
  static final String EXTRA_MEAL = packageName + ".EXTRA_MEAL";
  static final String EXTRA_BREAKFAST_TIME = packageName + ".EXTRA_BREAKFAST_TIME";
  static final String EXTRA_LUNCH_TIME = packageName + ".EXTRA_LUNCH_TIME";
  static final String EXTRA_DINNER_TIME = packageName + ".EXTRA_DINNER_TIME";

  private SharedPreferences sharedPrefs;
  private AlarmManager alarmManager;
  private NotificationManager notificationManager;

  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_drawer);
    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

    // Set default preferences
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    // Set up toolbar
    Toolbar toolbar = findViewById(R.id.main_toolbar);
    setSupportActionBar(toolbar);

    // Set up FAB
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Create an intent to open the AddRecipeActivity and start it
        Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
        ActivityOptionsCompat options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(intent, options.toBundle());
      }
    });

    // Set up Drawer
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.main_navDrawer);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);

    // Set up the RecyclerView's adapter
    recipeListAdapter = new RecipeListAdapter(this, imageViewModel);

    // Set onClick functionality
    recipeListAdapter.setRecipeClickedListener(new RecipeListAdapter.RecipeClickedListener() {
      @Override
      public void onRecipeClicked(Summary summary, ImageView imageView) {

        // Add the ID of the clicked recipe to the intent
        Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, summary.getRecipeId());

        // Set up transitions
        Pair<View, String> imagePair = Pair.create((View) imageView, "imageTransition");
        ActivityOptionsCompat optionsImageTransition =
            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair);
        ActivityOptionsCompat optionsActivityTransition =
            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);

        // Start the activity
        startActivity(intent, optionsActivityTransition.toBundle());
      }
    });

    // Set up RecyclerView
    RecyclerView recyclerView = findViewById(R.id.main_recycler);
    recyclerView.setAdapter(recipeListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Set up swipe and drag directions for the cards
    int swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

    // Set up ItemTouchHelper to handle swipes
    ItemTouchHelper touchHelper = new ItemTouchHelper(
        new ItemTouchHelper.SimpleCallback(0, swipeDirs) {
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        deleteRecipe(viewHolder.getAdapterPosition());
      }
    });

    // Attach the touch helper to the recyclerView
    touchHelper.attachToRecyclerView(recyclerView);

    summaryViewModel.getAllRecipeSummaries().observe(this, new Observer<List<Summary>>() {
      @Override
      public void onChanged(List<Summary> recipeSummaries) {
        recipeListAdapter.setSummaryList(recipeSummaries);
      }
    });

    imageViewModel.imageUpdateNotifier().observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(Integer integer) {
        Log.d(LOG_TAG, "I've notice that the data changed!");
        recipeListAdapter.notifyDataSetChanged();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Initialise system services
    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    boolean breakfastOn = sharedPrefs.getBoolean("breakfast_notification", false);
    boolean lunchOn = sharedPrefs.getBoolean("lunch_notification", false);
    boolean dinnerOn = sharedPrefs.getBoolean("dinner_notification", false);
    setUpMealReminders(breakfastOn, lunchOn, dinnerOn);
  }

  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  private void deleteRecipe(final int position) {
    final Summary recipeToDelete = recipeListAdapter.getRecipeIdAtPosition(position);
    String alertMessage =
        getString(R.string.main_dialog_delete) + " \"" + recipeToDelete.getTitle() + "\"?";

    // Set up dialog for user confirmation
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setMessage(alertMessage);

    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        summaryViewModel.deleteRecipe(recipeToDelete.getRecipeId());
        recipeListAdapter.notifyDataSetChanged();
      }
    });

    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        recipeListAdapter.notifyItemChanged(position);
      }
    });

    alertBuilder.show();
  }

  private void setAlarm(int hour, int min, PendingIntent pendingIntent, String extraMealKey) {
    // Get the AlarmManager service
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    // Create a calendar object
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());

    // Set the calendar time
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, min);

    // Create meal time variable
    int mealTime = hour * 100 + min;

    // Save meal time to shared prefs
    SharedPreferences.Editor sharedPrefEditor = sharedPrefs.edit();
    sharedPrefEditor.putInt(extraMealKey, mealTime);
    sharedPrefEditor.apply();

    // Set the alarm
    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        cal.getTimeInMillis(),
        AlarmManager.INTERVAL_DAY,
        pendingIntent);
  }

  private PendingIntent getNotifyPendingIntent(Intent notifyIntent) {
    return PendingIntent.getBroadcast(this, ALARM_NOTIF_ID,
        notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel = new NotificationChannel(
          ALARM_NOTIF_CHANNEL_ID,
          "Meal reminders",
          NotificationManager.IMPORTANCE_DEFAULT);

      // Configure initial channel settings
      notificationChannel.enableVibration(true);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.RED);
      notificationChannel.setDescription("Reminders to start making a meal");

      // Create the channel
      notificationManager.createNotificationChannel(notificationChannel);
    }
  }

  private boolean setUpMealReminders(boolean breakfastOn, boolean lunchOn, boolean dinnerOn) {
    // Set up breakfast, lunch, and dinner PendingIntents
    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
    PendingIntent breakfastNotifyPendingIntent = getNotifyPendingIntent(alarmIntent);
    PendingIntent lunchNotifyPendingIntent = getNotifyPendingIntent(alarmIntent);
    PendingIntent dinnerNotifyPendingIntent = getNotifyPendingIntent(alarmIntent);

    // Turn the alarm off if it should be, otherwise set up the alarms
    if (alarmManager != null) {
      if (!breakfastOn && !lunchOn && !dinnerOn) {
        alarmManager.cancel(breakfastNotifyPendingIntent);
        alarmManager.cancel(lunchNotifyPendingIntent);
        alarmManager.cancel(dinnerNotifyPendingIntent);
        Log.d(LOG_TAG, "All alarms off and cancelled");
        return false;
      }

      createNotificationChannel();

      // Turn on the breakfast alarm
      if (breakfastOn) {
        setAlarm(18, 0, breakfastNotifyPendingIntent, EXTRA_BREAKFAST_TIME);
        Log.d(LOG_TAG, "Breakfast alarm on");

      } else {
        alarmManager.cancel(breakfastNotifyPendingIntent);
        Log.d(LOG_TAG, "Breakfast alarm off");
      }

      // Turn on the lunch alarm
      if (lunchOn) {
        setAlarm(12, 0, lunchNotifyPendingIntent, EXTRA_LUNCH_TIME);
        Log.d(LOG_TAG, "Lunch alarm on");

      } else {
        alarmManager.cancel(lunchNotifyPendingIntent);
        Log.d(LOG_TAG, "Lunch alarm off");
      }

      // Turn on the dinner alarm
      if (dinnerOn) {
        setAlarm(17, 30, dinnerNotifyPendingIntent, EXTRA_DINNER_TIME);
        Log.d(LOG_TAG, "Dinner alarm on");

      } else {
        alarmManager.cancel(dinnerNotifyPendingIntent);
        Log.d(LOG_TAG, "Dinner alarm off");
      }
    }
    return true;
  }


  // - - - - - - - - - - - - - - - Options Menu methods - - - - - - - - - - - - - - -

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  // - - - - - - - - - - - - - - - Drawer methods - - - - - - - - - - - - - - -

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_menu_shoppingList) {
      Intent intent = new Intent(this, ShoppingListActivity.class);
      startActivity(intent);

    } else if (id == R.id.nav_settings) {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);

    } else {
      return  false;
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
