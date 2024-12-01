package com.example.zenithevents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FacilityDeleteTest {

    private FacilityUtils facilityUtils;
    private EventUtils eventUtils;
    private List<Event> testingEvents;
    private List<Facility> testingFacilities;

    @Before
    public void startingUp() {
        facilityUtils = new FacilityUtils();
        eventUtils = new EventUtils();
        testingEvents = new ArrayList<>();
        testingFacilities = new ArrayList<>();

        Event eventFirst = new Event();
        eventFirst.setEventId("event1");
        eventFirst.setEventName("Event 1");
        eventFirst.setNumParticipants(100);

        Event eventSecond = new Event();
        eventSecond.setEventId("event2");
        eventSecond.setEventName("Event 2");
        eventSecond.setNumParticipants(200);
        testingEvents.add(eventFirst);
        testingEvents.add(eventSecond);

        Facility facility = new Facility();
        facility.setDeviceId("facility1");
        facility.setNameOfFacility("Test Facility");
        facility.setEmailOfFacility("randomfacility123@gmail.com");
        facility.setPhoneOfFacility("0987654321");
        testingFacilities.add(facility);

    }
    @Test
    public void testGetEventsOfFacility() {
        List<Event> expectedEvents = testingEvents;
        List<Event> actualEvents = runningGettingEvents("facility1");
        assertNotNull(actualEvents);
        assertEquals(expectedEvents.size(), actualEvents.size());
    }

    @Test
    public void testDeleteEventsOfFacility() {
        List<Event> eventsDelete = new ArrayList<>(testingEvents);
        boolean result = runningDeletingEvents(eventsDelete);
        assertTrue(result);
        assertTrue(eventsDelete.isEmpty());
    }

    @Test
    public void testDeleteFacility() {
        String facilityId = "facility1";
        List<Event> eventsDelete = runningGettingEvents(facilityId);
        runningDeletingEvents(eventsDelete);
        boolean result = runningDeleteFacility(facilityId);
        assertTrue(result);
    }

    private List<Event> runningGettingEvents(String facilityId) {
        return testingEvents;
    }

    private boolean runningDeletingEvents(List<Event> events) {
        events.clear();
        return events.isEmpty();
    }

    private boolean runningDeleteFacility(String facilityId) {
        return testingFacilities.removeIf(facility -> facility.getDeviceId().equals(facilityId));
    }


}
