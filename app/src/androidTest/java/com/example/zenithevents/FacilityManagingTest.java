package com.example.zenithevents;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;
import com.example.zenithevents.Facility.CreateFacility;
import com.example.zenithevents.Facility.ViewFacility;
import com.example.zenithevents.HelperClasses.DeviceUtils;

import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;



public class FacilityManagingTest {

    @Rule
    public ActivityTestRule<CreateFacility> createFacilityRule = new ActivityTestRule<>(CreateFacility.class);

    @Test
    public void testCreateFacility() {
        String deviceId = DeviceUtils.getDeviceID(createFacilityRule.getActivity());


        onView(withId(R.id.location_name)).perform(typeText("Test Facility Name"));
        onView(withId(R.id.location_phone)).perform(typeText("1234567890"));
        onView(withId(R.id.location_email)).perform(typeText("test@example.com"));
        onView(withId(R.id.location_save)).perform(click());


        //Verify facility data in ViewFacility
        Intent intent = new Intent(createFacilityRule.getActivity(), ViewFacility.class);
        intent.putExtra("deviceId", deviceId);  // Use the actual device ID
        ActivityScenario<ViewFacility> scenario = ActivityScenario.launch(intent);

        // Check if the displayed data matches what was saved
        onView(withId(R.id.namefield)).check(matches(withText("Name: Test Facility Name")));
        onView(withId(R.id.phonefield)).check(matches(withText("Phone: 1234567890")));
        onView(withId(R.id.emailfield)).check(matches(withText("Email: test@example.com")));

    }


}
