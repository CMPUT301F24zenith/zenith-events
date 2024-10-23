package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Organization  {
    private String organizationName;
    private ArrayList<Event> events;

    public Organization(String organizationName) {
        this.organizationName = organizationName;
        this.events = new ArrayList<>();
    }

    public String getOrganizationName() {
        return organizationName;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public void addEvent(Event event) {
        events.add(event);
    }
    public void removeEvent(Event event) {
        events.remove(event);
    }
}
