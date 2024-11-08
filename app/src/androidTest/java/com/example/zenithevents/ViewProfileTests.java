package com.example.zenithevents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.User.UserProfile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewProfileTests {
    @Rule
    public ActivityScenarioRule<UserProfile> activityRule = new ActivityScenarioRule<>(UserProfile.class);

    @Test
    public void testEditProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.editFirstName)).perform(ViewActions.typeText("Test_EditFirstName"));
        Espresso.onView(ViewMatchers.withId(R.id.editLastName)).perform(ViewActions.typeText("Test_EditLastName"));
        Espresso.onView(ViewMatchers.withId(R.id.editEmail)).perform(ViewActions.typeText("Test_EditEmail"));
        Espresso.onView(ViewMatchers.withId(R.id.editPhoneNumber)).perform(ViewActions.typeText("Test_EditPhoneNumber"));
        Espresso.onView(ViewMatchers.withId(R.id.btnSave)).perform(ViewActions.click());


    }

    @Test
    public void testRemoveButton() {
        onView(withId(R.id.btnRemove)).perform(click());
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()));
    }

    @Test
    public void testUploadButton() {
        onView(withId(R.id.profileImage)).perform(click());
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()));
    }
}
