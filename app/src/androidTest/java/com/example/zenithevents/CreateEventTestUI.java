package com.example.zenithevents;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;

import android.content.Intent;

import com.example.zenithevents.R;

import com.example.zenithevents.Events.CreateEventPage;

public class CreateEventTestUI {

    @Rule
    public ActivityScenarioRule<CreateEventPage> activityScenarioRule =
            new ActivityScenarioRule<>(CreateEventPage.class);

    @Test
    public void testEventLimitValidation() {
        // Fill out the event details
        onView(withId(R.id.eventNameInput)).perform(typeText("Test Event"), closeSoftKeyboard());
        onView(withId(R.id.eventLocationInput)).perform(typeText("Test Location"), closeSoftKeyboard());
        onView(withId(R.id.eventDescriptionInput)).perform(typeText("Test Description"), closeSoftKeyboard());

        // Invalid event limit (0)
        onView(withId(R.id.eventLimitInput)).perform(typeText("0"), closeSoftKeyboard());

        // Attempt to save the event
        onView(withId(R.id.createEventSaveButton)).perform(click());

        // Verify that the error message appears for event limit
        onView(withId(R.id.eventLimitInput)).check(matches(hasErrorText("Limit can't be 0")));
    }

    @Test
    public void testEmptyEventName() {
        // Leave event name empty
        onView(withId(R.id.eventLocationInput)).perform(typeText("Test Location"), closeSoftKeyboard());
        onView(withId(R.id.eventDescriptionInput)).perform(typeText("Test Description"), closeSoftKeyboard());
        onView(withId(R.id.eventLimitInput)).perform(typeText("10"), closeSoftKeyboard());

        // Attempt to save the event
        onView(withId(R.id.createEventSaveButton)).perform(click());

        // Verify if the error message appears for empty event name
        onView(withId(R.id.eventNameInput)).check(matches(hasErrorText("Event Name can't be empty")));
    }

    @Test
    public void testEventNameInput() {
        // Create an Intent with a valid eventId (non-null)
        Intent intent = new Intent(getApplicationContext(), CreateEventPage.class);
        intent.putExtra("Event Id", "kR6gW0xoWTrVzmYrgZu5");  // Provide a valid event ID

        // Launch the activity with the intent
        ActivityScenario.launch(intent);

        // Fill out event name
        onView(withId(R.id.eventNameInput)).perform(typeText("class demo"), closeSoftKeyboard());

//         Verify if the event name input field contains the correct text
        onView(withId(R.id.eventNameInput)).check(matches(withText("class demo")));
    }



    @Test
    public void testGeolocationCheckbox() {
        // Toggle the geolocation checkbox
        onView(withId(R.id.geolocationCheckbox)).perform(click());

        // Verify if the checkbox is checked
        onView(withId(R.id.geolocationCheckbox)).check(matches(isChecked()));

        // Toggle the checkbox again
        onView(withId(R.id.geolocationCheckbox)).perform(click());

        // Verify if the checkbox is unchecked
        onView(withId(R.id.geolocationCheckbox)).check(matches(isChecked())); // It should be unchecked after toggle
    }

    @Test
    public void testCancelButton() {
        // Click the cancel button
        onView(withId(R.id.createEventCancelButton)).perform(click());

        // Check that the activity is finished, indicating that we are not in the CreateEventPage anymore
        // You could use IdlingResource or verify screen transitions here if necessary.
    }

    @Test
    public void testValidEventCreation() {
        // Fill out valid event details
        onView(withId(R.id.eventNameInput)).perform(typeText("Valid Event"), closeSoftKeyboard());
        onView(withId(R.id.eventLocationInput)).perform(typeText("Test Location"), closeSoftKeyboard());
        onView(withId(R.id.eventDescriptionInput)).perform(typeText("Test Description"), closeSoftKeyboard());
        onView(withId(R.id.eventLimitInput)).perform(typeText("10"), closeSoftKeyboard());

        // Select the geolocation checkbox
        onView(withId(R.id.geolocationCheckbox)).perform(click());

        // Attempt to save the event
        onView(withId(R.id.createEventSaveButton)).perform(click());

    }
}
