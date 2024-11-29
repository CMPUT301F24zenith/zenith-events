package com.example.zenithevents;


import com.example.zenithevents.Objects.User;

import org.junit.Test;

public class NotificationsTest {


    @Test
    public void testSendNotification() {
        User user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
        user.sendNotification("Test message");
    }
}
