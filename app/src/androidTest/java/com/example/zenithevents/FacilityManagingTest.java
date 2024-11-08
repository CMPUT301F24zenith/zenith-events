package com.example.zenithevents;

//import android.content.Intent;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.rule.ActivityTestRule;
//
//import com.example.zenithevents.Facility.CreateFacility;
//import com.example.zenithevents.Facility.ViewFacility;
//
//import org.junit.Rule;
//import org.junit.Test;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.example.zenithevents.Facility.CreateFacility;
import com.example.zenithevents.Facility.ViewFacility;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

import com.example.zenithevents.Facility.CreateFacility;
import com.example.zenithevents.Facility.ViewFacility;

import org.junit.Rule;
import org.junit.Test;

public class FacilityManagingTest {

    @Rule
    public ActivityTestRule<CreateFacility> createFacilityRule =
            new ActivityTestRule<>(CreateFacility.class);

    @Test
    public void testCreateFacility() {
        // Simulate typing information into text fields
        onView(withId(R.id.location_name)).perform(typeText("Test Facility Name"));
        onView(withId(R.id.location_phone)).perform(typeText("1234567890"));
        onView(withId(R.id.location_email)).perform(typeText("test@example.com"));

        // Click the save button
        onView(withId(R.id.location_save)).perform(click());

        // Check if it navigates to ViewFacility after saving
        Intent intent = new Intent(createFacilityRule.getActivity(), ViewFacility.class);
        ActivityScenario<ViewFacility> scenario = ActivityScenario.launch(intent);

        // Verify that ViewFacility is launched and displays correct information
        onView(withId(R.id.namefield)).check(matches(withText("Name: Test Facility Name")));
        onView(withId(R.id.phonefield)).check(matches(withText("Phone: 1234567890")));
        onView(withId(R.id.emailfield)).check(matches(withText("Email: test@example.com")));
    }
}
