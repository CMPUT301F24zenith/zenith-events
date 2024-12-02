package com.example.zenithevents;

import android.content.Context;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.User;

import org.junit.Test;

import java.util.ArrayList;

public class ReplacementTest {
    private Event event;
    private User user1, user2, user3, user4;
    private Context context;

    @Test
    public void testReplacement() {
        event = new Event();
        event.setNumParticipants(3);
        user1 = new User("TestID", "TestName", "TestLastName", "TestEmail", "TestPhone");
        user2 = new User("TestID2", "TestName", "TestLastName", "TestEmail", "TestPhone");
        user3 = new User("TestID3", "TestName", "TestLastName", "TestEmail", "TestPhone");
        user4 = new User("TestID4", "TestName", "TestLastName", "TestEmail", "TestPhone");
        ArrayList<String> waitingList = new ArrayList<>();
        ArrayList<String> selectedList = new ArrayList<>();

        waitingList.add(user1.getDeviceID());
        waitingList.add(user2.getDeviceID());
        waitingList.add(user3.getDeviceID());
        waitingList.add(user4.getDeviceID());
        event.setWaitingList(waitingList);

        selectedList.add(user1.getDeviceID());
        waitingList.remove(user1.getDeviceID());
        selectedList.add(user2.getDeviceID());
        waitingList.remove(user2.getDeviceID());
        selectedList.add(user3.getDeviceID());
        waitingList.remove(user3.getDeviceID());
        event.setSelected(selectedList);
        event.setWaitingList(waitingList);
        assert (event.getNumParticipants() == event.getSelected().size());
        assert(event.getWaitingList().size() == 1);

        selectedList.remove(user2.getDeviceID());
        event.setSelected(selectedList);
        assert (event.getSelected().size() == 2);
        selectedList.add(user4.getDeviceID());
        waitingList.remove(user4.getDeviceID());
        event.setSelected(selectedList);
        event.setWaitingList(waitingList);
        assert (event.getSelected().size() == 3);
        assert (event.getWaitingList().size() == 0);
    }
}
