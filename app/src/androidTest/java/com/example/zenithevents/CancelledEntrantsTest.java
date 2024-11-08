//package com.example.zenithevents;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.zenithevents.EntrantsList.CancelledEntrants;
//import com.example.zenithevents.Objects.User;
//import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CancelledEntrantsTest {
//
//    private CancelledEntrants cancelledEntrantsActivity;
//    private List<User> testCancelledUsers;
//
//    @Before
//    public void setUp() {
//        // Initialize the CancelledEntrants activity
//        cancelledEntrantsActivity = new CancelledEntrants();
//
//        // Set up sample cancelled users data
//        testCancelledUsers = new ArrayList<>();
//        testCancelledUsers.add(new User("device1", "User", "One", "user1@example.com", "1234567890"));
//        testCancelledUsers.add(new User("device2", "User", "Two", "user2@example.com", "0987654321"));
//
//        // Manually assign this test data to the activity's dataList for testing purposes
//        cancelledEntrantsActivity.dataList = new ArrayList<>(testCancelledUsers);
//    }
//
//    @Test
//    public void testCancelledEntrantsDataHandling() {
//        // Check that the list size matches the number of test users
//        assertEquals(2, cancelledEntrantsActivity.dataList.size());
//
//        // Validate the first user details
//        User firstUser = cancelledEntrantsActivity.dataList.get(0);
//        assertEquals("User One", firstUser.getFirstName() + " " + firstUser.getLastName());
//        assertEquals("user1@example.com", firstUser.getEmail());
//
//        // Validate the second user details
//        User secondUser = cancelledEntrantsActivity.dataList.get(1);
//        assertEquals("User Two", secondUser.getFirstName() + " " + secondUser.getLastName());
//        assertEquals("user2@example.com", secondUser.getEmail());
//
//        // Ensure the dataList is not null and contains expected values
//        assertNotNull(cancelledEntrantsActivity.dataList);
//    }
//
//
//}
