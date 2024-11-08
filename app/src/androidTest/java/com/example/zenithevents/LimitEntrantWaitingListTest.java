package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;


public class LimitEntrantWaitingListTest {
    @Test
    public void testLimit() {
        Event event = new Event();

        int participants_limit = 10;
        event.setNumParticipants(participants_limit);

        assertEquals(participants_limit, event.getNumParticipants());
    }

}

