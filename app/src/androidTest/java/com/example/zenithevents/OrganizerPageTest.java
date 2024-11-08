package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Facility.CreateFacility;
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
    public void testFacilityButton() {
        Espresso.onView(ViewMatchers.withId(R.id.organizerButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.createFacilityButton)).perform(ViewActions.click());
//        Intents.intended(IntentMatchers.hasComponent(CreateFacility.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.location_name)).perform(ViewActions.typeText("Test Facility"));
        Espresso.onView(ViewMatchers.withId(R.id.location_phone)).perform(ViewActions.typeText("1234567890"));
        Espresso.onView(ViewMatchers.withId(R.id.location_email)).perform(ViewActions.typeText("james.a.garfield@examplepetstore.com"));
        Espresso.onView(ViewMatchers.withId(R.id.location_save)).perform(ViewActions.click());
//        Intents.intended(IntentMatchers.hasComponent(OrganizerPage.class.getName()));
    }

    @Test
    public void testInputEventDetails() {
        Espresso.onView(ViewMatchers.withId(R.id.organizerButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.eventNameInput)).perform(ViewActions.typeText("Test Event"));
        Espresso.onView(ViewMatchers.withId(R.id.eventDescriptionInput)).perform(ViewActions.typeText("Test Description"));
        Espresso.onView(ViewMatchers.withId(R.id.eventLocationInput)).perform(ViewActions.typeText("Test Location"));
        Espresso.onView(ViewMatchers.withId(R.id.eventLimitInput)).perform(ViewActions.typeText("100"));
        Espresso.onView(ViewMatchers.withId(R.id.createEventSaveButton)).perform(ViewActions.click());

    }
}
