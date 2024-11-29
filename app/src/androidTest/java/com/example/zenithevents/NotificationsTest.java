package com.example.zenithevents;


import android.content.Context;

import com.example.zenithevents.Objects.User;

import org.junit.Test;

public class NotificationsTest {
    private Context context;


    @Test
    public void testSendNotificationWinner() {
        User user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
        user.sendNotification(context,"Congratulations! You have been selected for");

    }
    @Test
    public void testSendNotificationToAll() {
        User user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
        user.sendNotification(context,"You have not been selected for");
    }
}
