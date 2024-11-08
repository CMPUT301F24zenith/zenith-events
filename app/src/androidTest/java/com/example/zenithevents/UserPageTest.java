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

        Espresso.onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.btnSave)).check(doesNotExist());

    }

    @Test
    public void testRemoveProfileImg() {
        Espresso.onView(withId(R.id.btnRemove)).perform(click());

        onView(withId(R.id.btnRemove)).check(doesNotExist());
    }

}
