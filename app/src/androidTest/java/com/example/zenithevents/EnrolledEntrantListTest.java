package com.example.zenithevents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.EntrantsList.EnrolledEntrants;
import com.example.zenithevents.Objects.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EnrolledEntrantListTest {
    private List<User> testEnrolledEntrants;

    @Before
    public void setUp() {
        testEnrolledEntrants = new ArrayList<>();
        testEnrolledEntrants.add(new User("device1", "User", "One", "user1@example.com", "1234567890"));
        testEnrolledEntrants.add(new User("device2", "User", "Two", "user2@example.com", "0987654321"));

    }

    @Test
    public void testEnrolledEntrantList() {
        ActivityScenario<EnrolledEntrants> scenario = ActivityScenario.launch(EnrolledEntrants.class);
        scenario.onActivity(activity -> {
            try{
                Field dataListField = EnrolledEntrants.class.getDeclaredField("dataList");
                dataListField.setAccessible(true);
                dataListField.set(activity, new ArrayList<>(testEnrolledEntrants));

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
