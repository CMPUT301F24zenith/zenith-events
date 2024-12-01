//package com.example.zenithevents;
//
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.matcher.IntentMatchers;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//
//import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;
//import com.example.zenithevents.EntrantDashboard.EventsFragment;
//import com.example.zenithevents.User.OrganizerPage;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//public class EntrantDashboardTest {
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Before
//    public void setUp() {
//        Intents.init();
//
//    }
//    @After
//    public void tearDown() {
//        Intents.release();
//    }
//
//    @Test
//    public void testEventFragmentButton() {
//        Espresso.onView(ViewMatchers.withId(R.id.entrantButton)).perform(ViewActions.click());
//        Intents.intended(IntentMatchers.hasComponent(EntrantViewActivity.class.getName()));
////        Espresso.onView(ViewMatchers.withId(R.id.btnEvents)).check(matches(isDisplayed()));
//
//    }
//}
