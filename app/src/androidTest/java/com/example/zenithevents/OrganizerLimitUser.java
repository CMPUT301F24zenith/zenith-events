package com.example.zenithevents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;

public class OrganizerLimitUser {
    @Test
    public void testOrganizerLimitUser() {
        Event event = new Event();
        assertEquals(0, event.getNumParticipants());
        event.setNumParticipants(10);
        assertEquals(10, event.getNumParticipants());
    }
}
