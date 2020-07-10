package com.gerardbradshaw.mater.activities.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gerardbradshaw.mater.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

  private static final String ALARM_NOTIF_CHANNEL_ID = MainActivity.ALARM_NOTIFICATION_CHANNEL_ID;
  private static final int ALARM_NOTIF_ID = MainActivity.ALARM_NOTIFICATION_ID;
  private final String LOG_TAG = "GGG - AlarmReceiver";

  private int currentTime = -1;


  @Override
  public void onReceive(Context context, Intent intent) {
    deliverNotification(context, getMeal(context));
  }

  private void deliverNotification(Context context, String meal) {
    // Get the notification manager
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Create content intent and corresponding pending intent
    Intent contentIntent = new Intent(context, MainActivity.class);
    PendingIntent contentPendingIntent = PendingIntent.getActivity(
        context,
        ALARM_NOTIF_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT);

    // Build the notification
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, ALARM_NOTIF_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Meal reminder")
            .setContentText("It's time to start cooking " + meal + "!")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH);

    notificationManager.notify(ALARM_NOTIF_ID, builder.build());
  }

  private String getMeal(Context context) {
    // Save the current time
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    currentTime = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
    Log.d(LOG_TAG, "Current time: " + currentTime);

    // Get the saved times for meals
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    int breakfastTime = sharedPrefs.getInt(MainActivity.EXTRA_BREAKFAST_TIME, -100);
    int lunchTime = sharedPrefs.getInt(MainActivity.EXTRA_LUNCH_TIME, -100);
    int dinnerTime = sharedPrefs.getInt(MainActivity.EXTRA_DINNER_TIME, -100);
    Log.d(LOG_TAG, "Breakfast time: " + breakfastTime
        + ", lunch time: " + lunchTime + ", dinner time: " + dinnerTime);

    if (isMeal(breakfastTime) && !isMeal(lunchTime) && !isMeal(dinnerTime)) {
      return "breakfast";

    } else if (isMeal(lunchTime) && !isMeal(breakfastTime) && !isMeal(dinnerTime)) {
      return "lunch";

    } else if (isMeal(dinnerTime) && !isMeal(breakfastTime) && !isMeal(lunchTime)) {
      return "dinner";

    }
    return "a meal";
  }

  /**
   * Returns true if the meal was 5 minutes ago or in 10 minutes
   * @param mealTime the meal time to check
   * @return true if it was within specified parameters
   */
  private boolean isMeal(int mealTime) {
    return currentTime < mealTime + 10 && currentTime > mealTime - 5;
  }
}
