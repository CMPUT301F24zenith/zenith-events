package com.example.zenithevents;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntrantUITest {
    @Rule
    public ActivityScenarioRule<EntrantViewActivity> activityRule = new ActivityScenarioRule<>(EntrantViewActivity.class);

    @Before
    public void setUp() {
        // Perform any necessary setup before each test
        Intents.init();
    }
    @After
    public void tearDown() {
        // Perform any necessary cleanup after each test
        Intents.release();
    }

    @Test
    public void testManageProfileButton() {
        onView(ViewMatchers.withId(R.id.viewProfileButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editFirstName))
                .perform(ViewActions.scrollTo(), ViewActions.typeText("Test_EditFirstName"));
        onView(ViewMatchers.withId(R.id.editLastName)).perform(ViewActions.typeText("Test_EditLastName"));
        onView(ViewMatchers.withId(R.id.editEmail)).perform(ViewActions.typeText("Test_EditEmail"));
        onView(ViewMatchers.withId(R.id.editPhoneNumber)).perform(ViewActions.typeText("Test_EditPhoneNumber"));
        onView(ViewMatchers.withId(R.id.btnSave)).perform(ViewActions.click());


    }
}
