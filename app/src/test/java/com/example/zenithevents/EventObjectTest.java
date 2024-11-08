package com.example.zenithevents;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;

import java.util.ArrayList;

public class EventObjectTest {
    @Test
    public void testEventObject() {
        Event event = new Event();
        event.setEventTitle("Test Event");
        event.setOwnerFacility("Test Facility");
        event.setNumParticipants(10);
        event.setWaitingList(new ArrayList<>());
        event.setSelected(new ArrayList<>());
        event.setRegistrants(new ArrayList<>());

        assert event.getEventTitle().equals("Test Event");
        assert event.getOwnerFacility().equals("Test Facility");
        assert event.getNumParticipants() == 10;
        assert event.getWaitingList().isEmpty();
        assert event.getSelected().isEmpty();
        assert event.getRegistrants().isEmpty();

    }
}
