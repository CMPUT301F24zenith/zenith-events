package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;

public class LimitWaitingTest {
    @Test
    public void testLimitWaiting() {
        // Create a new Event object
        Event event = new Event();
        event.setEventTitle("Test Event");

        int participantsLimit = 10;
        event.setNumParticipants(participantsLimit);

        assertEquals(participantsLimit, event.getNumParticipants());
    }


}
