package com.example.zenithevents;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Admin.AdminViewActivity;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.Objects.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

public class AdminUITests {

    private Event event1, event2, event3;
    private User user1, user2, user3;
    private Facility facility1, facility2, facility3;
    private ArrayList<Facility> facilityList;
    private ArrayList<User> userList;
    private ArrayList<Event> eventList;
    private ArrayList<Event> ImageURLs;

    @Test
    public void adminRemovesEvent(){
        event1 = new Event();
        event2 = new Event();
        event3 = new Event();
        eventList = new ArrayList<>();
        event1.setEventId("1");
        event2.setEventId("2");
        event3.setEventId("3");

        eventList.add(event1);
        eventList.add(event2);
        eventList.add(event3);

        String deleteID = event3.getEventId();
        for (Event event : eventList){
            if (event.getEventId().equals(deleteID)){
                eventList.remove(event);
            }
        }
        assert(eventList.size() == 2);
    }

    @Test
    public void adminRemovesProfiles(){
        user1 = new User();
        user2 = new User();
        user3 = new User();
        userList = new ArrayList<>();
        user1.setDeviceID("1");
        user2.setDeviceID("2");
        user3.setDeviceID("3");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        String deleteID = user3.getDeviceID();

        for (User user : userList){
            if (user.getDeviceID().equals(deleteID)){
                userList.remove(user);
            }
        }
        assert(userList.size() == 2);

    }

    @Test
    public void adminRemovesFacility(){
        facility1 = new Facility();
        facility2 = new Facility();
        facility3 = new Facility();
        facilityList = new ArrayList<>();

        facility1.setNameOfFacility("1");
        facility2.setNameOfFacility("2");
        facility3.setNameOfFacility("3");
        facilityList.add(facility1);
        facilityList.add(facility2);
        facilityList.add(facility3);

        String deleteID = facility3.getNameOfFacility();

        for (Facility facility : facilityList){
            if (facility.getNameOfFacility().equals(deleteID)) {
                facilityList.remove(facility);
            }
            }
        assert(facilityList.size() == 2);
    }

    @Test
    public void AdminRemovesImages(){
        event1 = new Event();
        event2 = new Event();
        event3 = new Event();
        ImageURLs = new ArrayList<>();
        event1.setImageUrl("1");
        event2.setImageUrl("2");
        event3.setImageUrl("3");

        ImageURLs.add(event1);
        ImageURLs.add(event2);
        ImageURLs.add(event3);

        String deleteImage = event2.getImageUrl();

        for (Event event : ImageURLs){
            if (event.getImageUrl().equals(deleteImage)){
                event.setImageUrl("");
            }
        }
        assert(event2.getImageUrl() == "");


    }

}
