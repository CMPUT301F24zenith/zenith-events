//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import static org.hamcrest.CoreMatchers.is;
//import static java.util.function.Predicate.not;
//
//import android.view.View;
//
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.ViewAction;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.zenithevents.MainActivity;
//import com.example.zenithevents.R;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class DeclineEventTest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testDeclineEventInvitation() {
//        // 1. Assuming the UI already has a "Decline" button for a single event
//        // Let's click on the "Decline" button.
//
//        onView(withId(R.id.declineEventBtn))  // Replace with your actual decline button ID
//                .perform(click());  // Perform a click on the button
//
//        // 2. Check if the Toast message appears confirming the event was declined.
//        onView(withText("Declined Event Invitation"))  // Replace with your actual Toast message
//                .inRoot(withDecorView(not(is(activityRule.getScenario().getActivity().getWindow().getDecorView()))))
//                .check(matches(isDisplayed()));  // Verify the toast is shown
//
//        // 3. Optionally, check if the event is removed from the UI or list
//        // Assuming your event's title or other data is displayed somewhere:
//        onView(withText("Event Title"))  // Replace with actual event title text
//                .check(matches(not(isDisplayed())));  // Check if the event title is no longer visible
//    }
//}
