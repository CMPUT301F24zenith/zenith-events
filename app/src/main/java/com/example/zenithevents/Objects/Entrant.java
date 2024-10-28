package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Entrant {

    private String role;
    private String entrantId;
    private String entrantFirstName;
    private String entrantLastName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> entrantEvents;
    private String profileImageURL;
    private Boolean wantsNotifs;

    public Entrant(String role, String entrantId, String entrantFirstName, String entrantLastName, String email, String phoneNumber) {

        this.role = role;
        this.entrantId = entrantId;
        this.entrantFirstName = entrantFirstName;
        this.entrantLastName = entrantLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public String getRole() {
        return role;
    }

    public String getEntrantId() {
        return entrantId;
    }

    public String getEntrantFirstName() {
        return entrantFirstName;
    }

    public String getEntrantLastName() {
        return entrantLastName;
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

    public ArrayList<Event> getEntrantEvents() {
        return entrantEvents;
    }

    
    public void setRole(String role) {
        this.role = role;
    }

    public void setEntrantId(String entrantId) {
        this.entrantId = entrantId;
    }

    public void setEntrantFirstName(String entrantFirstName) {
        this.entrantFirstName = entrantFirstName;
    }

    public void setEntrantLastName(String entrantLastName) {
        this.entrantLastName = entrantLastName;
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

    public void enterWaitingList(Event event) {
        event.getWaitingList().add(this);
        entrantEvents.add(event);
    }

    public void leaveWaitingList(Event event) {
        event.getWaitingList().remove(this);
        entrantEvents.remove(event);
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
