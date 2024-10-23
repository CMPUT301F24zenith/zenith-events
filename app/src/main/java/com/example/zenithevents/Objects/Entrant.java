package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Entrant {
    private String entrantName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> entrantEvents;
    private String profileImageURL;
    private Boolean wantsNotifs;

    public Entrant(String entrantName, String email, String phoneNumber, Boolean wantsNotifs) {
        this.entrantName = entrantName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.wantsNotifs = wantsNotifs;
        this.entrantEvents = new ArrayList<>();
        }

    public String getEntrantName() {
        return entrantName;
    }
    public String getProfileImageURL() {
        return profileImageURL;
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
