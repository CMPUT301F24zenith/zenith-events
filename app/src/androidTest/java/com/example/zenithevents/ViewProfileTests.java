package com.example.zenithevents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.User.UserPage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewProfileTests {
    @Rule
    public ActivityScenarioRule<UserPage> activityRule = new ActivityScenarioRule<>(UserPage.class);

    @Test
    public void testRemoveButton() {
        onView(withId(R.id.btnRemove)).perform(click());
    }
}
