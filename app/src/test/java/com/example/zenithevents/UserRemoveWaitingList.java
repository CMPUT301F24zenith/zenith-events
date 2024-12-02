package com.example.zenithevents;

import com.example.zenithevents.Objects.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

public class UserRemoveWaitingList {

    private Event testingEvent;
    private String testingDeviceId;

    @Before
    public void startUp() {
        testingEvent = new Event();
        testingDeviceId = "devicerandome123";
        ArrayList<String> startingWaitingList = new ArrayList<>();
        startingWaitingList.add(testingDeviceId);
        testingEvent.setWaitingList(startingWaitingList);
    }

    @Test
    public void removingUserWaitingList() {
        assertTrue(testingEvent.getWaitingList().contains(testingDeviceId));
        testingEvent.getWaitingList().remove(testingDeviceId);
        assertFalse(testingEvent.getWaitingList().contains(testingDeviceId));
    }


}
