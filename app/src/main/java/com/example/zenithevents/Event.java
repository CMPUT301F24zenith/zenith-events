package com.example.zenithevents;

import java.util.ArrayList;
import java.util.Collections;

public class Event {
    private long eventId;
    private Organizer creator;
    private ArrayList<Entrant> waitingList;
    private ArrayList<Entrant> selected;
    private ArrayList<Entrant> registrants;
    private int numParticipants;


    public ArrayList<Entrant> getWaitingList() {
        return waitingList;
    }

    public ArrayList<Entrant> getSelected() {
        return selected;
    }

    // Get numParticipants number of selected from waiting list
    public ArrayList<Entrant> drawLottery() {
        Collections.shuffle(waitingList);

        int numToSelect = Math.min(numParticipants, waitingList.size());

        ArrayList<Entrant> drawEntrants = new ArrayList<>(waitingList.subList(0, numToSelect));

        selected.addAll(drawEntrants);

        waitingList.removeAll(drawEntrants);

        return drawEntrants;
    }

    public void sendNotifications(String message, ArrayList<Entrant> recipients){
        // Send notifications to recipients
        for (Entrant recipient : recipients) {
            recipient.sendNotification(message);
        }
    }
}
