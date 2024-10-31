package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class User {

    private String deviceID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> entrantEvents;
    private String profileImageURL;
    private Boolean wantsNotifs;
    private Boolean isAdmin;
    private String myFacility;

    public User(String deviceID, String firstName, String lastName, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = false;
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

    public ArrayList<Event> getEntrantEvents() {
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

    public void enterWaitingList(Event event) {
        event.getWaitingList().add(this);
        entrantEvents.add(event);
    }

    public void leaveWaitingList(Event event) {
        event.getWaitingList().remove(this);
        entrantEvents.remove(event);
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

    public void setEntrantEvents(ArrayList<Event> entrantEvents) {
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

    boolean isSelected(Event event) {
        return event.getSelected().contains(this);
    }
    boolean wantsNotifs() {
        return wantsNotifs;
    }

}
