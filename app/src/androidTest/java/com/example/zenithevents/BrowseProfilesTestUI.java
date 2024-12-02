package com.example.zenithevents.Admin;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

//need to make sure phone in admin mode
@RunWith(AndroidJUnit4.class)
public class BrowseProfilesTestUI {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void GotoAdminProfiles() throws InterruptedException {
//        time added incase card takes long to load
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.adminCard))
                .perform(click());
        Espresso.onView(withId(R.id.listView)).check(matches(isDisplayed()));

    }

}
