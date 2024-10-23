package com.example.zenithevents.Objects;


import java.util.ArrayList;
import java.util.Collections;

public class Event {
    private String eventId;
    private ArrayList<Entrant> waitingList;
    private ArrayList<Entrant> selected;
    private ArrayList<Entrant> registrants;
    private String eventImage;
    private String QRCodeURL;
    private int numParticipants;

    public Event(String eventId, String eventImage, String QRCodeURL, int numParticipants){
        this.eventId = eventId;
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.registrants = new ArrayList<>();
        this.eventImage = eventImage;
        this.QRCodeURL = QRCodeURL;
        this.numParticipants = numParticipants;
    }

    public String getEventId() {
        return eventId;
    }

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
    public ArrayList<Entrant> getRegistrants() {
        return registrants;
    }
    public int getNumParticipants() {
        return numParticipants;
    }
    public String getQRCodeURL() {
        return QRCodeURL;
    }

    public void sendNotifications(String message, ArrayList<Entrant> recipients){
        // Send notifications to recipients
        for (Entrant recipient : recipients) {
            if (recipient.wantsNotifs()){
            recipient.sendNotification(message);}
        }
    }
    public String getEventImage() {
        return eventImage;
    }
}
