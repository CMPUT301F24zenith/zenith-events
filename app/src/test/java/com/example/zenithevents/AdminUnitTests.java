package com.example.zenithevents;


import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.Objects.User;

import org.junit.Test;

import java.util.ArrayList;

public class AdminUnitTests {

    private Event event1, event2, event3;
    private User user1, user2, user3;
    private Facility facility1, facility2, facility3;
    private ArrayList<Facility> facilityList;
    private ArrayList<User> userList;
    private ArrayList<Event> eventList;
    private ArrayList<Event> ImageURLs;

    @Test
    public void adminRemovesEvent(){
        // Events created for testing
        event1 = new Event();
        event2 = new Event();
        event3 = new Event();
        eventList = new ArrayList<>();
        event1.setEventId("1");
        event2.setEventId("2");
        event3.setEventId("3");
        // Event list
        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);

        String deleteID = event3.getEventId();
        Event deleteEvent = new Event();
        // Admin wants to delete event 3
        for (Event event : eventList){
            if (event.getEventId().equals(deleteID)){
                deleteEvent = event;


            }
        }
        // Admin deletes event 3
        eventList.remove(deleteEvent);
        assert(eventList.size() == 2);
    }

    @Test
    public void adminRemovesProfiles(){
        // Profiles created for testing
        user1 = new User();
        user2 = new User();
        user3 = new User();
        userList = new ArrayList<>();
        user1.setDeviceID("1");
        user2.setDeviceID("2");
        user3.setDeviceID("3");
        // User list
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        String deleteID = user3.getDeviceID();
        User deleteUser = new User();
        // Admin wants to delete user 3
        for (User user : userList){
            if (user.getDeviceID().equals(deleteID)){
                deleteUser = user;

            }
        }
        // Admin deletes user 3
        userList.remove(deleteUser);
        assert(userList.size() == 2);

    }

    @Test
    public void adminRemovesFacility(){
        // Facilities created for testing
        facility1 = new Facility();
        facility2 = new Facility();
        facility3 = new Facility();
        facilityList = new ArrayList<>();

        facility1.setNameOfFacility("1");
        facility2.setNameOfFacility("2");
        facility3.setNameOfFacility("3");
        // Facility list
        facilityList.add(facility1);
        facilityList.add(facility2);
        facilityList.add(facility3);

        String deleteID = facility3.getNameOfFacility();

        Facility deleteFacility = new Facility();
        // Admin wants to delete facility 3
        for (Facility facility : facilityList){
            if (facility.getNameOfFacility().equals(deleteID)) {
                deleteFacility = facility;
            }
            }
        // Admin deletes facility 3
        facilityList.remove(deleteFacility);
        assert(facilityList.size() == 2);
    }

    @Test
    public void AdminRemovesImages(){
        // Events created for testing
        event1 = new Event();
        event2 = new Event();
        event3 = new Event();
        ImageURLs = new ArrayList<>();
        event1.setImageUrl("1");
        event2.setImageUrl("2");
        event3.setImageUrl("3");
        // Event list
        ImageURLs.add(event1);
        ImageURLs.add(event2);
        ImageURLs.add(event3);

        String deleteImage = event2.getImageUrl();
        // Admin wants to delete image 2
        for (Event event : ImageURLs){
            // Admin deletes image 2
            if (event.getImageUrl().equals(deleteImage)){
                event.setImageUrl("");
            }
        }
        assert(event2.getImageUrl() == "");


    }

}
