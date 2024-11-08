package com.example.zenithevents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.EntrantsList.CancelledEntrants;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;

import org.junit.Before;
import org.junit.Test;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CancelledEntrantsTest {

    private List<User> testCancelledUsers;

    @Before
    public void setUp() {
        // Set up sample cancelled users data
        testCancelledUsers = new ArrayList<>();
        testCancelledUsers.add(new User("device1", "User", "One", "user1@example.com", "1234567890"));
        testCancelledUsers.add(new User("device2", "User", "Two", "user2@example.com", "0987654321"));
    }

    @Test
    public void testCancelledEntrantsDataHandling() {
        // Launch the CancelledEntrants activity
        ActivityScenario<CancelledEntrants> scenario = ActivityScenario.launch(CancelledEntrants.class);

        scenario.onActivity(activity -> {
            try {
                // Use reflection to set the private `dataList` field with test data
                Field dataListField = CancelledEntrants.class.getDeclaredField("dataList");
                dataListField.setAccessible(true);
                dataListField.set(activity, new ArrayList<>(testCancelledUsers));

                // Verify the data
                assertNotNull(dataListField.get(activity));
                List<User> dataList = (List<User>) dataListField.get(activity);

                // Check the list size
                assertEquals(2, dataList.size());

                // Validate the first user details
                User firstUser = dataList.get(0);
                assertEquals("User One", firstUser.getFirstName() + " " + firstUser.getLastName());
                assertEquals("user1@example.com", firstUser.getEmail());

                // Validate the second user details
                User secondUser = dataList.get(1);
                assertEquals("User Two", secondUser.getFirstName() + " " + secondUser.getLastName());
                assertEquals("user2@example.com", secondUser.getEmail());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
