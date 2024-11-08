package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class User {

    private String deviceID, profileImageURL, firstName, lastName, email, phoneNumber, myFacility;
    private ArrayList<String> entrantEvents, cancelledEvents, selectedEvents, waitingEvents;
    private Boolean wantsNotifs, isAdmin;

    // No-argument constructor required for Firestore
    public User() {}
    public User(String deviceID, String firstName, String lastName, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = false;
        this.entrantEvents = new ArrayList<>();
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

    public ArrayList<String> getEntrantEvents() {
        return entrantEvents;
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

    public void setEntrantEvents(ArrayList<String> entrantEvents) {
        this.entrantEvents = entrantEvents;
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

    public ArrayList<String> getCancelledEvents() {
        return cancelledEvents;
    }

    public void setCancelledEvents(ArrayList<String> cancelledEvents) {
        this.cancelledEvents = cancelledEvents;
    }

    public ArrayList<String> getSelectedEvents() {
        return selectedEvents;
    }

    public void setSelectedEvents(ArrayList<String> selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    public ArrayList<String> getWaitingEvents() {
        return waitingEvents;
    }

    public void setWaitingEvents(ArrayList<String> waitingEvents) {
        this.waitingEvents = waitingEvents;
    }
}