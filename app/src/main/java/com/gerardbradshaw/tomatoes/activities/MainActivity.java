package com.gerardbradshaw.tomatoes.activities;

import android.content.Intent;
import android.os.Bundle;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.activities.adapters.RecipeListAdapter;
import com.gerardbradshaw.tomatoes.room.entities.Summary;
import com.gerardbradshaw.tomatoes.viewmodels.ImageViewModel;
import com.gerardbradshaw.tomatoes.viewmodels.SummaryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
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

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Layout views
  private RecyclerView recyclerView;
  private RecipeListAdapter recipeListAdapter;

  // Data objects
  private SummaryViewModel summaryViewModel;
  private ImageViewModel imageViewModel;

  // Intent extras
  public static final String EXTRA_RECIPE_ID = "com.gerardbradshaw.tomatoes.EXTRA_RECIPE_ID";

  // Logging
  private static String LOG_TAG = "GGG - Main Activity";

  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_drawer);
    summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

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
        startActivity(intent);
      }
    });

    // Set up Drawer
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.main_nav_view);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            MainActivity.this, imagePair);

        // Start the activity
        startActivity(intent, options.toBundle());
      }
    });

    // Set up RecyclerView
    recyclerView = findViewById(R.id.main_recycler);
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
        // Get the position of the item swiped
        int position = viewHolder.getAdapterPosition();

        // Determine which recipe was swiped
        int recipeToDelete = recipeListAdapter.getRecipeIdAtPosition(position);

        summaryViewModel.deleteRecipe(recipeToDelete);
        recipeListAdapter.notifyDataSetChanged();
        // TODO confirmation prompt for the user

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



  // - - - - - - - - - - - - - - - Options Menu methods - - - - - - - - - - - - - - -

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
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

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_home) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_tools) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
