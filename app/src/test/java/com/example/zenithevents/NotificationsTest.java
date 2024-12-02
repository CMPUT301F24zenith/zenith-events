package com.example.zenithevents;


import android.content.Context;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.User;

import org.junit.Test;

import java.util.ArrayList;

public class NotificationsTest {
    private Context context;


    @Test
    public void testSendNotificationWinner() {
        User user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
        user.sendNotification(context,"Congratulations! You have been selected for");
    }
    @Test
    public void testSendNotificationLoser() {
        User user = new User("deviceID", "firstName", "lastName", "email", "phoneNumber");
        user.sendNotification(context,"You have not been selected for");
    }
    @Test
    public void testSendNotificationToSelected() {
        ArrayList<User> selected = new ArrayList<User>();
        User user1 = new User("deviceID1", "firstName", "lastName", "email", "phoneNumber");
        User user2 = new User("deviceID2", "firstName", "lastName", "email", "phoneNumber");
        User user3 = new User("deviceID3", "firstName", "lastName", "email", "phoneNumber");
        selected.add(user1);
        selected.add(user2);
        selected.add(user3);

        Event event = new Event();
        event.setSelected(new ArrayList<String>());
        event.getSelected().add(user1.getDeviceID());
        event.getSelected().add(user2.getDeviceID());
        event.getSelected().add(user3.getDeviceID());

        for (String deviceID : event.getSelected()) {
            for (User user : selected) {
                if (user.getDeviceID().equals(deviceID)) {
                    user.sendNotification(context,"You have been selected for");
                }
            }
        }
    }

    @Test
    public void testSendNotificationToWaitlist() {
        ArrayList<User> waitingList = new ArrayList<User>();
        User user1 = new User("deviceID1", "firstName", "lastName", "email", "phoneNumber");
        User user2 = new User("deviceID2", "firstName", "lastName", "email", "phoneNumber");
        User user3 = new User("deviceID3", "firstName", "lastName", "email", "phoneNumber");
        waitingList.add(user1);
        waitingList.add(user2);
        waitingList.add(user3);

        Event event = new Event();
        event.setSelected(new ArrayList<String>());
        event.getSelected().add(user1.getDeviceID());
        event.getSelected().add(user2.getDeviceID());
        event.getSelected().add(user3.getDeviceID());

        for (String deviceID : event.getSelected()) {
            for (User user : waitingList) {
                if (user.getDeviceID().equals(deviceID)) {
                    user.sendNotification(context,"Notifs to waitingList");
                }
            }
        }
    }

    @Test
    public void testSendNotificationToCancelledList() {
        ArrayList<User> cancelledList = new ArrayList<User>();
        User user1 = new User("deviceID1", "firstName", "lastName", "email", "phoneNumber");
        User user2 = new User("deviceID2", "firstName", "lastName", "email", "phoneNumber");
        User user3 = new User("deviceID3", "firstName", "lastName", "email", "phoneNumber");
        cancelledList.add(user1);
        cancelledList.add(user2);
        cancelledList.add(user3);

        Event event = new Event();
        event.setSelected(new ArrayList<String>());
        event.getSelected().add(user1.getDeviceID());
        event.getSelected().add(user2.getDeviceID());
        event.getSelected().add(user3.getDeviceID());

        for (String deviceID : event.getSelected()) {
            for (User user : cancelledList) {
                if (user.getDeviceID().equals(deviceID)) {
                    user.sendNotification(context,"Notifs to cancelledList");
                }
            }
        }
    }

    @Test
    public void testSendNotificationToSignUP() {
        ArrayList<User> signUpList = new ArrayList<User>();
        User user1 = new User("deviceID1", "firstName", "lastName", "email", "phoneNumber");
        User user2 = new User("deviceID2", "firstName", "lastName", "email", "phoneNumber");
        User user3 = new User("deviceID3", "firstName", "lastName", "email", "phoneNumber");
        signUpList.add(user1);
        signUpList.add(user2);
        signUpList.add(user3);

        Event event = new Event();
        event.setSelected(new ArrayList<String>());
        event.getSelected().add(user1.getDeviceID());
        event.getSelected().add(user2.getDeviceID());
        event.getSelected().add(user3.getDeviceID());

        for (String deviceID : event.getSelected()) {
            for (User user : signUpList) {
                if (user.getDeviceID().equals(deviceID)) {
                    user.sendNotification(context,"Notifs to signUpList");
                }
            }
        }
    }
}
