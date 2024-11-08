package com.example.zenithevents;

import static org.junit.Assert.assertTrue;

import com.example.zenithevents.Objects.User;

import org.junit.Before;
import org.junit.Test;

public class UserJoinWaitingListTest {
    private User user;
    @Before
    public void setUp() {
        user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
    }

    @Test
    public void testJoinWaitingList() {
        String eventID = "eventID";


        user.getWaitingEvents().add("eventID");
        assertTrue(user.getWaitingEvents().contains("eventID"));
    }


}
