package com.example.zenithevents.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Notification implements Serializable {
    String userId;
    private ArrayList<String> messages, eventIds, subjects, eventNames;

    public Notification(String userId){
        this.messages = new ArrayList<>();
        this.eventIds = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.eventNames = new ArrayList<>();
    }

    public Notification(String userId, ArrayList<String> messages, ArrayList<String> eventNames, ArrayList<String> eventIds, ArrayList<String> subjects){
        this.userId = userId;
        this.messages = messages;
        this.eventIds = eventIds;
        this.subjects = subjects;
        this.eventNames = subjects;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getEventIds() {
        return eventIds;
    }

    public void setEventIds(ArrayList<String> eventIds) {
        this.eventIds = eventIds;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<String> getEventNames() {
        return eventNames;
    }

    public void setEventNames(ArrayList<String> eventNames) {
        this.eventNames = eventNames;
    }
}