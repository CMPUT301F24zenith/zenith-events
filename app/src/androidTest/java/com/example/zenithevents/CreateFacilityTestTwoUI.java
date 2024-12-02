package com.example.zenithevents.User;


import androidx.test.espresso.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;

import com.example.zenithevents.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateFacilityTestTwoUI {

    @Rule
    public ActivityScenarioRule<CreateFacility> activityRule = new ActivityScenarioRule<>(CreateFacility.class);

    @Test
    public void testFacilitySaveButton() {
        // Type some valid data into the EditText fields

        onView(withId(R.id.facility_name))
                .perform(typeText("Some Name"));

        onView(withId(R.id.facility_phone))
                .perform(typeText("123-456-7890"));

        onView(withId(R.id.facility_email))
                .perform(typeText("facility@example.com"));

        // Close the keyboard after typing
        Espresso.closeSoftKeyboard();

        // Click the Save button
        onView(withId(R.id.facility_save))
                .perform(click());

        // Check if the validation is passing and fields are saved properly
        onView(withId(R.id.facility_name))
                .check(matches(withText("Some Name")));

        onView(withId(R.id.facility_phone))
                .check(matches(withText("123-456-7890")));

        onView(withId(R.id.facility_email))
                .check(matches(withText("facility@example.com")));
    }

    @Test
    public void testEmptyFacilityNameValidation() {
        // Click Save button without filling out the name
        onView(withId(R.id.facility_save)).perform(click());

        // Check that the validation error appears for facility name
        onView(withId(R.id.facility_name))
                .check(matches(hasErrorText("Field is required.")));
    }

    @Test
    public void testEmptyFacilityEmailValidation() {
        // Type valid phone and name, but leave email empty
        onView(withId(R.id.facility_name))
                .perform(typeText("Some Name"));

        onView(withId(R.id.facility_phone))
                .perform(typeText("123-456-7890"));

        // Leave email empty and click save
        onView(withId(R.id.facility_save)).perform(click());

        // Check that the validation error appears for the email
        onView(withId(R.id.facility_email))
                .check(matches(hasErrorText("Email is required")));
    }

    @Test
    public void testInvalidEmailFormat() {
        // Type valid name and phone, but invalid email format
        onView(withId(R.id.facility_name))
                .perform(typeText("Some Name"));

        onView(withId(R.id.facility_phone))
                .perform(typeText("123-456-7890"));

        onView(withId(R.id.facility_email))
                .perform(typeText("invalid-email"));

        // Click the Save button
        onView(withId(R.id.facility_save)).perform(click());

        // Check that the validation error appears for email format
        onView(withId(R.id.facility_email))
                .check(matches(hasErrorText("Invalid email format")));
    }
}
