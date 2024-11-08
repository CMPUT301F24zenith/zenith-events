package com.example.zenithevents;

import androidx.test.rule.ActivityTestRule;
import com.example.zenithevents.Facility.CreateFacility;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



public class FacilityManagingTest {

    @Rule
    public ActivityTestRule<CreateFacility> createFacilityRule = new ActivityTestRule<>(CreateFacility.class);

    @Test
    public void testCreateFacility() {

        onView(withId(R.id.location_name)).perform(typeText("Test Facility Name"));
        onView(withId(R.id.location_phone)).perform(typeText("1234567890"));
        onView(withId(R.id.location_email)).perform(typeText("test@example.com"));


        onView(withId(R.id.location_save)).perform(click());


    }


}
