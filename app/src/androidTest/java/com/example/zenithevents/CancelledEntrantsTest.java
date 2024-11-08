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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class CancelledEntrantsTest {

    @Rule
    public ActivityTestRule<CancelledEntrants> activityRule = new ActivityTestRule<>(CancelledEntrants.class);

    private FirebaseFirestore db;
    private final String testEventId = "testEvent123"; // Predefined event ID

    @Before
    public void setUp() throws InterruptedException {
        db = FirebaseFirestore.getInstance();


        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);


        CountDownLatch latch = new CountDownLatch(1);


        Map<String, Object> eventData = new HashMap<>();
        eventData.put("cancelledList", Arrays.asList("user1", "user2"));

        db.collection("events").document(testEventId)
                .set(eventData)
                .addOnCompleteListener(task -> latch.countDown());

        latch.await(3, TimeUnit.SECONDS);
    }

    @Test
    public void testShowCancelledEntrantsList() {
        // Launch the CancelledEntrants activity with the testEventId
        Intent intent = new Intent(activityRule.getActivity(), CancelledEntrants.class);
        intent.putExtra("eventId", testEventId);

        try (ActivityScenario<CancelledEntrants> scenario = ActivityScenario.launch(intent)) {
            onData(anything()).inAdapterView(withId(R.id.list_entrants))
                    .atPosition(0).onChildView(withId(R.id.entrantName))
                    .check(matches(withText("User One")));

            onData(anything()).inAdapterView(withId(R.id.list_entrants))
                    .atPosition(1).onChildView(withId(R.id.entrantName))
                    .check(matches(withText("User Two")));
        }
    }
}
