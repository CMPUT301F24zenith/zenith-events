package com.example.zenithevents.Admin;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.zenithevents.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class BrowseEventsTestUI {

    @Rule
    public ActivityScenarioRule<AdminViewActivity> activityScenarioRule = new ActivityScenarioRule<>(AdminViewActivity.class);

    @Test
    public void GotoAdminEvents() {
        Espresso.onView(withId(R.id.btnEvents))
                .perform(click());
        Espresso.onView(withId(R.id.eventsListView))
                .check(matches(isDisplayed()));
    }

}
