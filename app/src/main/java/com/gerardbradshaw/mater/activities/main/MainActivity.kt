package com.gerardbradshaw.mater.activities.main

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gerardbradshaw.mater.R
import com.gerardbradshaw.mater.activities.addrecipe.AddRecipeActivity
import com.gerardbradshaw.mater.activities.recipedetail.RecipeDetailActivity
import com.gerardbradshaw.mater.activities.settings.SettingsActivity
import com.gerardbradshaw.mater.activities.shoppinglist.ShoppingListActivity
import com.gerardbradshaw.mater.room.entities.Summary
import com.gerardbradshaw.mater.viewmodels.ImageViewModel
import com.gerardbradshaw.mater.viewmodels.SummaryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  private var recipeListAdapter: RecipeListAdapter? = null
  private var summaryViewModel: SummaryViewModel? = null
  private var imageViewModel: ImageViewModel? = null
  private lateinit var sharedPrefs: SharedPreferences
  private var alarmManager: AlarmManager? = null
  private var notificationManager: NotificationManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_drawer)
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel::class.java)
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)

    configureNavigationViews()

    recipeListAdapter = RecipeListAdapter(this, imageViewModel)
    configureRecipeListAdapterOnClick(recipeListAdapter!!)

    val recyclerView = findViewById<RecyclerView>(R.id.main_recycler)
    configureRecipeListRecyclerView(recyclerView)
  }

  private fun configureNavigationViews() {
    val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
    val fab = findViewById<FloatingActionButton>(R.id.fab)
    setSupportActionBar(toolbar)

    fab.setOnClickListener {
      val intent = Intent(this@MainActivity, AddRecipeActivity::class.java)
      val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)
      startActivity(intent, options.toBundle())
    }

    val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
    val navigationView = findViewById<NavigationView>(R.id.main_navDrawer)
    val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)

    drawer.addDrawerListener(toggle)
    toggle.syncState()
    navigationView.setNavigationItemSelectedListener(this)
  }

  private fun configureRecipeListAdapterOnClick(recipeListAdapter: RecipeListAdapter) {
    recipeListAdapter.setRecipeClickedListener { summary, imageView -> // Add the ID of the clicked recipe to the intent
      val intent = Intent(this@MainActivity, RecipeDetailActivity::class.java)
      intent.putExtra(EXTRA_RECIPE_ID, summary.recipeId)

      // Set up transitions
      val imagePair = Pair.create(imageView as View, "imageTransition")
      val optionsImageTransition = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, imagePair)
      val optionsActivityTransition = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)

      // Start the activity
      startActivity(intent, optionsActivityTransition.toBundle())
    }
  }

  private fun configureRecipeListRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = recipeListAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)

    // Set up swipe and drag directions for the cards
    val swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

    // Set up ItemTouchHelper to handle swipes
    val touchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0, swipeDirs) {
          override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder,
                              t: RecyclerView.ViewHolder): Boolean {
            return false
          }

          override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            displayDeleteRecipeConfirmationAlert(viewHolder.adapterPosition)
          }
        })

    // Attach the touch helper to the recyclerView
    touchHelper.attachToRecyclerView(recyclerView)
    summaryViewModel!!.allRecipeSummaries.observe(this, Observer { recipeSummaries -> recipeListAdapter!!.setSummaryList(recipeSummaries) })
    imageViewModel!!.imageUpdateNotifier().observe(this, Observer {
      Log.d(LOG_TAG, "I've notice that the data changed!")
      recipeListAdapter!!.notifyDataSetChanged()
    })
  }

  override fun onResume() {
    super.onResume()
    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
    alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val isBreakfastReminderOn = sharedPrefs.getBoolean("breakfast_notification", false)
    val isLunchReminderOn = sharedPrefs.getBoolean("lunch_notification", false)
    val isDinnerReminderOn = sharedPrefs.getBoolean("dinner_notification", false)

    setUpMealReminders(isBreakfastReminderOn, isLunchReminderOn, isDinnerReminderOn)
  }


  // ---------------- Delete recipes ----------------

  private fun displayDeleteRecipeConfirmationAlert(position: Int) {
    val recipeToDelete = recipeListAdapter!!.getRecipeIdAtPosition(position)
    val message = getString(R.string.main_dialog_delete) + " \"" + recipeToDelete.title + "\"?"

    AlertDialog.Builder(this).also {
      it.setMessage(message)
      it.setPositiveButton("Confirm") { _, _ -> deleteRecipe(recipeToDelete) }
      it.setNegativeButton("Cancel") { _, _ -> recipeListAdapter!!.notifyItemChanged(position) }
      it.show()
    }
  }

  private fun deleteRecipe(recipeToDelete: Summary) {
    summaryViewModel!!.deleteRecipe(recipeToDelete.recipeId)
    recipeListAdapter!!.notifyDataSetChanged()
  }


  // ---------------- Notifications ----------------

  private fun setAlarm(hour: Int, min: Int, pendingIntent: PendingIntent, extraMealKey: String) {
    sharedPrefs.edit().also {
      it.putInt(extraMealKey, hour * 100 + min)
      it.apply()
    }

    val cal = Calendar.getInstance().also {
      it.timeInMillis = System.currentTimeMillis()
      it[Calendar.HOUR_OF_DAY] = hour
      it[Calendar.MINUTE] = min
    }

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
  }

  private fun getNotifyPendingIntent(notifyIntent: Intent): PendingIntent {
    return PendingIntent.getBroadcast(this,
        ALARM_NOTIFICATION_ID,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationChannel = NotificationChannel(ALARM_NOTIFICATION_CHANNEL_ID, "Meal reminders",
          NotificationManager.IMPORTANCE_DEFAULT).also {
        it.enableVibration(true)
        it.enableLights(true)
        it.lightColor = Color.RED
        it.description = "Reminders to start making a meal"
      }

      notificationManager!!.createNotificationChannel(notificationChannel)
    }
  }

  private fun setUpMealReminders(isBreakfastAlarmOn: Boolean, isLunchAlarmOn: Boolean, isDinnerAlarmOn: Boolean) {
    val notifyIntent = Intent(this, AlarmReceiver::class.java)
    val breakfastNotificationPendingIntent = getNotifyPendingIntent(notifyIntent)
    val lunchNotificationPendingIntent = getNotifyPendingIntent(notifyIntent)
    val dinnerNotificationPendingIntent = getNotifyPendingIntent(notifyIntent)

    if (alarmManager != null) {
      createNotificationChannel()

      if (isBreakfastAlarmOn) setAlarm(14, 20, breakfastNotificationPendingIntent, EXTRA_BREAKFAST_TIME)
      else alarmManager!!.cancel(breakfastNotificationPendingIntent)

      if (isLunchAlarmOn) setAlarm(12, 0, lunchNotificationPendingIntent, EXTRA_LUNCH_TIME)
      else alarmManager!!.cancel(lunchNotificationPendingIntent)

      if (isDinnerAlarmOn) setAlarm(17, 30, dinnerNotificationPendingIntent, EXTRA_DINNER_TIME)
      else alarmManager!!.cancel(dinnerNotificationPendingIntent)

      Log.d(LOG_TAG, "Breakfast alarm on: $isBreakfastAlarmOn")
      Log.d(LOG_TAG, "Lunch alarm on: $isLunchAlarmOn")
      Log.d(LOG_TAG, "Dinner alarm on: $isDinnerAlarmOn")
    }
  }


  // ---------------- Options menu ----------------

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_settings -> {
        startActivity(Intent(this, SettingsActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  // ---------------- Navigation drawer ----------------

  override fun onBackPressed() {
    val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

    if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START)
    else super.onBackPressed()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.nav_menu_shoppingList -> startActivity(Intent(this, ShoppingListActivity::class.java))
      R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
      else -> return false
    }

    val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
    drawer.closeDrawer(GravityCompat.START)
    return true
  }

  companion object {
    private const val LOG_TAG = "GGG - Main Activity"
    const val ALARM_NOTIFICATION_CHANNEL_ID = "com.gerardbradshaw.mater.ALARM_NOTIFICATION_CHANNEL_ID"
    const val ALARM_NOTIFICATION_ID = 1
    private const val packageName = "com.gerardbradshaw.mater"
    const val EXTRA_RECIPE_ID = "$packageName.EXTRA_RECIPE_ID"
    const val EXTRA_BREAKFAST_TIME = "$packageName.EXTRA_BREAKFAST_TIME"
    const val EXTRA_LUNCH_TIME = "$packageName.EXTRA_LUNCH_TIME"
    const val EXTRA_DINNER_TIME = "$packageName.EXTRA_DINNER_TIME"
  }
}