package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.User.OrganizerPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrganizerPageTest {
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
    public void testOrganizerButton() {
        Espresso.onView(ViewMatchers.withId(R.id.organizerButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(OrganizerPage.class.getName()));
    }

    @Test
    public void testCreateEventButton() {
        Espresso.onView(ViewMatchers.withId(R.id.organizerButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(CreateEventPage.class.getName()));
    }
}
