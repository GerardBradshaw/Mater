package com.gerardbradshaw.mater;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.gerardbradshaw.mater.activities.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeLoadingTest {

  @Rule
  public ActivityTestRule activityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void ActivityLaunch() {

    // TODO get the name of the recipe at position 0

    onView(withId(R.id.main_recycler))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    // TODO assert that the title of the next screen is the name of the correct recipe
  }

}
