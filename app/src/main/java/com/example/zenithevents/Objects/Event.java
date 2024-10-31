package com.example.zenithevents.Objects;


import java.util.ArrayList;
import java.util.Collections;

public class Event {
    private String eventId;
    private ArrayList<User> waitingList;
    private ArrayList<User> selected;
    private ArrayList<User> registrants;
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

    public ArrayList<User> getWaitingList() {
        return waitingList;
    }

    public ArrayList<User> getSelected() {
        return selected;
    }

    // Get numParticipants number of selected from waiting list
    public ArrayList<User> drawLottery() {
        Collections.shuffle(waitingList);

        int numToSelect = Math.min(numParticipants, waitingList.size());

        ArrayList<User> drawUsers = new ArrayList<>(waitingList.subList(0, numToSelect));

        selected.addAll(drawUsers);

        waitingList.removeAll(drawUsers);

        return drawUsers;
    }
    public ArrayList<User> getRegistrants() {
        return registrants;
    }
    public int getNumParticipants() {
        return numParticipants;
    }
    public String getQRCodeURL() {
        return QRCodeURL;
    }

    public void sendNotifications(String message, ArrayList<User> recipients){
        // Send notifications to recipients
        for (User recipient : recipients) {
            if (recipient.wantsNotifs()){
            recipient.sendNotification(message);}
        }
    }
    public String getEventImage() {
        return eventImage;
    }
}
