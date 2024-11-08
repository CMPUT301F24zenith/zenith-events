package com.example.zenithevents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;



import com.example.zenithevents.User.UserProfile;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewProfileTests {
    @Rule

    public ActivityScenarioRule<UserProfile> activityRule = new ActivityScenarioRule<>(UserProfile.class);


    @Test
    public void testRemoveButton() {
        onView(withId(R.id.btnRemove)).perform(click());
    }
}
