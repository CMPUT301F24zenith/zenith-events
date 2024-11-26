package com.example.zenithevents;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;

import java.util.ArrayList;
import java.util.List;

public class FacilityDeleteTest {

    private FacilityUtils facilityUtils;
    private EventUtils eventUtils;
    private List<Event> testingEvents;
    private List<Facility> testingFacilities;

    public void startingUp() {
        facilityUtils = new FacilityUtils();
        eventUtils = new EventUtils();
        testingEvents = new ArrayList<>();

        Event eventFirst = new Event();
        eventFirst.setEventId("event1");
        eventFirst.setEventTitle("Event 1");
        eventFirst.setNumParticipants(100);

        Event eventSecond = new Event();
        eventSecond.setEventId("event2");
        eventSecond.setEventTitle("Event 2");
        eventSecond.setNumParticipants(200);
    }
}
