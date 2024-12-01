package com.example.zenithevents;
import androidx.test.core.app.ActivityScenario;
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
import com.example.zenithevents.User.CreateProfileActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateProfileTestUI {

    @Rule
    public ActivityScenarioRule<CreateProfileActivity> activityRule =
            new ActivityScenarioRule<>(CreateProfileActivity.class);

    @Test
    public void profileFieldsCheck() {
        ActivityScenario.launch(CreateProfileActivity.class);
        onView(withId(R.id.etEntrantFirstName))
                .check(matches(isDisplayed()));
        onView(withId(R.id.etEntrantLastName))
                .check(matches(isDisplayed()));
        onView(withId(R.id.etEntrantEmail))
                .check(matches(isDisplayed()));
        onView(withId(R.id.etEntrantPhoneNumber))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btnEntrantConfirm))
                .check(matches(isEnabled()));
        onView(withId(R.id.btnEntrantConfirm))
                .perform(click());

        onView(withId(R.id.etEntrantFirstName))
                .check(matches(hasErrorText("First name is required")));
        onView(withId(R.id.etEntrantLastName))
                .check(matches(hasErrorText("Last name is required")));
        onView(withId(R.id.etEntrantEmail))
                .check(matches(hasErrorText("Email is required")));
        onView(withId(R.id.etEntrantFirstName))
                .perform(typeText("John"));
        onView(withId(R.id.etEntrantLastName))
                .perform(typeText("Doe"));
        onView(withId(R.id.etEntrantEmail))
                .perform(typeText("wrong-email"));
        onView(withId(R.id.btnEntrantConfirm))
                .perform(click());

        onView(withId(R.id.etEntrantEmail))
                .check(matches(hasErrorText("Invalid email format")));
        onView(withId(R.id.etEntrantEmail))
                .perform(clearText(), typeText("doejohn@gmail.com"));
        onView(withId(R.id.etEntrantPhoneNumber))
                .perform(typeText("1234567890"));
        onView(withId(R.id.btnEntrantConfirm))
                .perform(click());

    }
}
