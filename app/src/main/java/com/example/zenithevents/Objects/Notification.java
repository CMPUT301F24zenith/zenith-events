package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Notification {
    private String eventId;
    private String subject;
    private String message;
    private ArrayList<String> entrants;

    public Notification(String eventId, String subject, String message, ArrayList<String> entrants) {
        this.eventId = eventId;
        this.subject = subject;
        this.message = message;
        this.entrants = entrants;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getEntrants() {
        return entrants;
    }

    public void setEntrants(ArrayList<String> entrants) {
        this.entrants = entrants;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
