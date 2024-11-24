package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.User.CreateFacility;
import com.example.zenithevents.User.OrganizerPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrganizerUITest {
    @Rule
    public ActivityScenarioRule<OrganizerPage> scenarioRule = new ActivityScenarioRule<> (OrganizerPage.class);

    @Before
    public void setUp(){
        Intents.init();
    }
    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void testCreateEvent(){
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(CreateEventPage.class.getName()));
    }
    @Test
    public void testViewFacilityEvent(){
        Espresso.onView(ViewMatchers.withId(R.id.createFacilityButton)).perform(ViewActions.click());
        Intents.intended(IntentMatchers.hasComponent(CreateFacility.class.getName()));
    }

}
