package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EntrantDashboardTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();

    }
    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testEventFragmentButton() {
        Espresso.onView(ViewMatchers.withId(R.id.organizerCard)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(EntrantViewActivity.class.getName()));
//        Espresso.onView(ViewMatchers.withId(R.id.btnEvents)).check(matches(isDisplayed()));

    }
}
