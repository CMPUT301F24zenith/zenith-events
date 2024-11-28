package com.example.zenithevents;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileDeleteTest {

    private List<String> cancelledList;
    private List<String> registeredList;
    private List<String> selectedList;
    private List<String> waitlistedList;

    private String userIdToDelete;

    @Before
    public void setUp() {
        // Initialize lists with sample data
        cancelledList = new ArrayList<>(Arrays.asList("user123", "user456", "user789"));
        registeredList = new ArrayList<>(Arrays.asList("user123", "user234", "user890"));
        selectedList = new ArrayList<>(Arrays.asList("user123", "user567", "user890"));
        waitlistedList = new ArrayList<>(Arrays.asList("user123", "user678", "user999"));

        userIdToDelete = "user123";
    }

    @Test
    public void testDeleteUserFromLists() {
        // Remove the user from all lists
        removeUserFromLists(userIdToDelete);

        // Verify that the user has been removed from all the lists
        assertFalse(cancelledList.contains(userIdToDelete));
        assertFalse(registeredList.contains(userIdToDelete));
        assertFalse(selectedList.contains(userIdToDelete));
        assertFalse(waitlistedList.contains(userIdToDelete));
    }

    @Test
    public void testDeleteUserCompletely() {
        // First, remove the user from all lists
        removeUserFromLists(userIdToDelete);

        // Now, delete the user completely (here we just remove from an internal list as simulation)
        boolean deletionResult = deleteUser(userIdToDelete);

        // Verify that the user is deleted successfully
        assertTrue(deletionResult);
    }

    // Simulate removing user from lists
    private void removeUserFromLists(String userId) {
        cancelledList.remove(userId);
        registeredList.remove(userId);
        selectedList.remove(userId);
        waitlistedList.remove(userId);
    }

    // Simulate deleting the user completely
    private boolean deleteUser(String userId) {
        // In a real application, this would involve database deletion logic, but here we simulate it
        return !cancelledList.contains(userId) && !registeredList.contains(userId) &&
                !selectedList.contains(userId) && !waitlistedList.contains(userId);
    }
}
