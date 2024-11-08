package com.example.zenithevents.Objects;

import android.app.Activity;

import java.util.ArrayList;

public class User {

    private String deviceID, firstName, lastName, email;
    private String phoneNumber;
    private String profileImageURL;
    private Boolean wantsNotifs;
    private Boolean isAdmin;
    private String myFacility;
    private ArrayList<String> waitingEvents;
    private ArrayList<String> selectedEvents;
    private ArrayList<String> registeredEvents;
    private ArrayList<String> cancelledEvents;
    private String anonymousAuthID ;

    // No-argument constructor required for Firestore
    public User() {}
    public User(String deviceID, String firstName, String lastName, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = false;
        this.registeredEvents = new ArrayList<>();
        this.cancelledEvents = new ArrayList<>();
        this.selectedEvents = new ArrayList<>();
        this.waitingEvents = new ArrayList<>();
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public Boolean getWantsNotifs() {
        return wantsNotifs;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public String getMyFacility() {
        return myFacility;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public void setWantsNotifs(Boolean wantsNotifs) {
        this.wantsNotifs = wantsNotifs;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setMyFacility(String myFacility) {
        this.myFacility = myFacility;
    }

    // TODO
    public void sendNotification(String message) {

    }

    boolean wantsNotifs() {
        return wantsNotifs;
    }

    public ArrayList<String> getCancelledEvents() {
        return cancelledEvents;
    }

    public ArrayList<String> getRegisteredEvents() {
        return registeredEvents;
    }

    public ArrayList<String> getSelectedEvents() {
        return selectedEvents;
    }

    public ArrayList<String> getWaitingEvents() {
        return waitingEvents;
    }

    public void setWaitingEvents(ArrayList<String> waitingEvents) {
        this.waitingEvents = waitingEvents;
    }

    public void setSelectedEvents(ArrayList<String> selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    public void setRegisteredEvents(ArrayList<String> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    public void setCancelledEvents(ArrayList<String> cancelledEvents) {
        this.cancelledEvents = cancelledEvents;
    }
    public String getAnonymousAuthID() {
        return anonymousAuthID;
    }
    public void setAnonymousAuthID(String anonymousAuthID) {
        this.anonymousAuthID = anonymousAuthID;
    }

}
