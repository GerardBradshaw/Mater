package com.gerardbradshaw.mater.activities.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gerardbradshaw.mater.R;

public class AlarmReceiver extends BroadcastReceiver {

  private static final String ALARM_NOTIF_CHANNEL_ID = MainActivity.ALARM_NOTIF_CHANNEL_ID;
  private static final int ALARM_NOTIF_ID = MainActivity.ALARM_NOTIF_ID;
  private final String LOG_TAG = "GGG - AlarmReceiver";


  @Override
  public void onReceive(Context context, Intent intent) {
    String meal = null;

    Bundle intentExtras = intent.getExtras();
    for (String key : intentExtras.keySet()) {
      Log.d(LOG_TAG, key + " was included in the intent");
    }

    if (intent.hasExtra(MainActivity.EXTRA_MEAL)) {
      meal = intent.getStringExtra(MainActivity.EXTRA_MEAL);
    }

    if (meal == null || meal.isEmpty()) {
      meal = "a meal";
    }

    deliverNotification(context, meal);
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
}
