package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.User.UserProfile;


@RunWith(AndroidJUnit4.class)
public class UserPageTest {
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
    public void testCreateProfileButton() {

        Espresso.onView(ViewMatchers.withId(R.id.entrantButton)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.viewProfileButton)).perform(ViewActions.click());

//      Intents.intended(IntentMatchers.hasComponent(CreateEventPage.class.getName()));

        Espresso.onView(ViewMatchers.withId(R.id.etEntrantFirstName)).perform(ViewActions.typeText("Test_FirstName"));
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantFirstName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantLastName)).perform(ViewActions.typeText("Test_LastName"));
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantLastName)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantEmail)).perform(ViewActions.typeText("Test@email.com"));
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantEmail)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantPhoneNumber)).perform(ViewActions.typeText("12345678"));
        Espresso.onView(ViewMatchers.withId(R.id.etEntrantPhoneNumber)).perform(ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btnEntrantConfirm)).perform(ViewActions.click());
//      Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));


//        Espresso.onView(ViewMatchers.withId(R.id.viewProfileButton)).perform(ViewActions.click());
//        Intents.intended(IntentMatchers.hasComponent(UserPage.class.getName()));
    }

}
