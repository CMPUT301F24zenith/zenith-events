package com.example.zenithevents;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.zenithevents.User.UserPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserPageTest {
    @Rule
    public ActivityScenarioRule<UserPage> activityRule = new ActivityScenarioRule<UserPage>(UserPage.class);

//    @Before
//    public void setUp() {
//        Intents.init();
//    }
//
//    @After
//    public void tearDown() {
//        Intents.release();
//    }

    @Test
    public void testSaveProfile() {

//        onView(withId(R.id.editFirstName)).perform(typeText("John"));
//        onView(withId(R.id.editLastName)).perform(typeText("Doe"));
//        onView(withId(R.id.editEmail)).perform(typeText("william.henry.harrison@example-pet-store.com"));
//        onView(withId(R.id.editPhoneNumber)).perform(typeText("1234567890"));
        Espresso.onView(withId(R.id.btnSave)).perform(click());

        onView(withId(R.id.btnSave)).check(doesNotExist());
//        onView(withText("Profile saved successfully!")).check(matches(isDisplayed()));


    }
}
