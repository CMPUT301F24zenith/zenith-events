package com.example.zenithevents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.User.UserPage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewProfileTests {
    @Rule
    public ActivityScenarioRule<UserPage> activityRule = new ActivityScenarioRule<>(UserPage.class);

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
    }
}
