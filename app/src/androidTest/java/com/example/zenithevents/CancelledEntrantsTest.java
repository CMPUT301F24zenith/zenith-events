package com.example.zenithevents;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.zenithevents.EntrantsList.CancelledEntrants;
import com.example.zenithevents.Objects.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class CancelledEntrantsTest {

    @Rule
    public ActivityTestRule<CancelledEntrants> activityRule = new ActivityTestRule<>(CancelledEntrants.class);

    private FirebaseFirestore db;
    private String generatedEventId;

    @Before
    public void setUp() throws InterruptedException {
        db = FirebaseFirestore.getInstance();

        // Configure Firebase for testing
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false) // Disable local persistence
                .build();
        db.setFirestoreSettings(settings);

        // Use CountDownLatch to wait for asynchronous Firestore tasks
        CountDownLatch latch = new CountDownLatch(3);

        // Create mock users
        db.collection("users").document("user1")
                .set(new User("device1", "User", "One", "user1@example.com", "1234567890"))
                .addOnCompleteListener(task -> latch.countDown());

        db.collection("users").document("user2")
                .set(new User("device2", "User", "Two", "user2@example.com", "0987654321"))
                .addOnCompleteListener(task -> latch.countDown());

        // Create an event and get its auto-generated ID
        db.collection("events")
                .add(new HashMap<String, Object>() {{
                    put("cancelledList", Arrays.asList("user1", "user2"));
                }})
                .addOnSuccessListener(documentReference -> {
                    generatedEventId = documentReference.getId(); // Capture the event ID
                    latch.countDown();
                });

        // Wait for all Firestore operations to complete before running the test
        latch.await();
    }

    @Test
    public void testShowCancelledEntrantsList() {
        // Launch the CancelledEntrants activity with the dynamically generated eventId
        Intent intent = new Intent(activityRule.getActivity(), CancelledEntrants.class);
        intent.putExtra("eventId", generatedEventId);

        try (ActivityScenario<CancelledEntrants> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                // Use runOnUiThread to ensure the test does not block the main thread
                activity.runOnUiThread(() -> {
                    onData(anything()).inAdapterView(withId(R.id.list_entrants))
                            .atPosition(0).onChildView(withId(R.id.entrantName))
                            .check(matches(withText("User One")));

                    onData(anything()).inAdapterView(withId(R.id.list_entrants))
                            .atPosition(1).onChildView(withId(R.id.entrantName))
                            .check(matches(withText("User Two")));
                });
            });
        }
    }
}
