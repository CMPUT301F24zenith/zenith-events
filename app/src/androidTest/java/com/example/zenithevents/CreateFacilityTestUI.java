package com.example.zenithevents;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
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
import com.example.zenithevents.User.CreateFacility;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateFacilityTestUI {

    @Rule
    public ActivityScenarioRule<CreateFacility> activityRule = new ActivityScenarioRule<>(CreateFacility.class);

    @Test
    public void emptyFacilityName() {
        onView(withId(R.id.facility_save))
                .perform(click());
        onView(withId(R.id.facility_name))
                .check(matches(hasErrorText("Field is required.")));
    }

    @Test
    public void emptyFacilityEmail() {
        onView(withId(R.id.facility_name))
                .perform(typeText("Some Name"));
        onView(withId(R.id.facility_phone))
                .perform(typeText("123-451-0910"));
        onView(withId(R.id.facility_save))
                .perform(click());
        onView(withId(R.id.facility_email))
                .check(matches(hasErrorText("Email is required")));
    }

    @Test
    public void wrongFacilityEmailFormat() {
        onView(withId(R.id.facility_name))
                .perform(typeText("Some Name"));
        onView(withId(R.id.facility_phone))
                .perform(typeText("123-451-0910"));
        onView(withId(R.id.facility_email))
                .perform(typeText("wrong-info"));
        onView(withId(R.id.facility_save))
                .perform(click());
        onView(withId(R.id.facility_email))
                .check(matches(hasErrorText("Invalid email format")));
    }
}
