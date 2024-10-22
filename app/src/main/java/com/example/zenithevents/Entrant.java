package com.example.zenithevents;

import java.util.ArrayList;

public class Entrant {
    private ArrayList<Event> entrantEvents;
    public void enterWatitingList(Event event) {
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

}
