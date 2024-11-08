package com.example.zenithevents;

import static android.content.Intent.getIntent;

import static org.junit.Assert.assertTrue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Objects.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateEventPageTest {
    @Rule
    public ActivityScenarioRule<CreateEventPage> activityRule = new ActivityScenarioRule<>(CreateEventPage.class);

    @Before
    public void setUp() {
        Event event = new Event();
        assertTrue(event != null);
        event.setImageUrl(String.valueOf(R.drawable.event_place_holder));

    }
    @Test
    public void testCancelEventButton() {
        Espresso.onView(ViewMatchers.withId(R.id.createEventCancelButton)).perform(ViewActions.click());

    }

    @Test
    public void testCreateEventSaveButton() {

        Espresso.onView(ViewMatchers.withId(R.id.eventNameInput)).perform(ViewActions.typeText("Test_EventName"));
        Espresso.onView(ViewMatchers.withId(R.id.eventNameInput)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.eventDescriptionInput)).perform(ViewActions.typeText("Test_EventDescription"));
        Espresso.onView(ViewMatchers.withId(R.id.eventDescriptionInput)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.eventLocationInput)).perform(ViewActions.typeText("Test_Location"));
        Espresso.onView(ViewMatchers.withId(R.id.eventLocationInput)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.eventLimitInput)).perform(ViewActions.typeText("10"));
        Espresso.onView(ViewMatchers.withId(R.id.eventLimitInput)).perform(ViewActions.closeSoftKeyboard());
//        Espresso.onView(ViewMatchers.withId(R.id.eventPosterImage)).perform(ViewActions.ImageView(R.drawable.event_place_holder));
//        Espresso.onView(ViewMatchers.withId(R.id.createEventSaveButton)).perform(ViewActions.click());
    }


}
