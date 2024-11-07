package com.example.zenithevents.Objects;


import java.util.ArrayList;
import java.util.Collections;

public class Event {
    private String eventId;
    private ArrayList<String> waitingList;
    private ArrayList<String> selected;
    private ArrayList<String> registrants;
    private String ImageUrl;
    private String QRCodeURL;
    private int numParticipants;
    private String eventTitle;
    private String ownerFacility;
    private String eventAddress;


    public Event(String eventTitle, String eventId, String eventAddress,String ImageUrl, int numParticipants) {
        this.eventTitle = eventTitle;
        this.eventId = eventId;
        this.ImageUrl = ImageUrl;
        this.numParticipants = numParticipants;
        this.eventAddress = eventAddress;

    }

    public Event() {

    }

    public String getEventId() {
        return eventId;
    }

    public ArrayList<String> getWaitingList() {
        return waitingList;
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    // Get numParticipants number of selected from waiting list
    public ArrayList<String> drawLottery() {
        Collections.shuffle(waitingList);

        int numToSelect = Math.min(numParticipants, waitingList.size());

        ArrayList<String> drawUsers = new ArrayList<>(waitingList.subList(0, numToSelect));

        selected.addAll(drawUsers);

        waitingList.removeAll(drawUsers);

        return drawUsers;
    }
    public ArrayList<String> getRegistrants() {
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
    public String getImageUrl() {
        return ImageUrl;
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public void setQRCodeURL(String QRCodeURL) {
        this.QRCodeURL = QRCodeURL;
    }
    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }
    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
        }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public void setWaitingList(ArrayList<String> waitingList) {
        this.waitingList = waitingList;
    }
    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
        }
    public void setRegistrants(ArrayList<String> registrants) {
        this.registrants = registrants;
    }
    public void setOwnerFacility(String ownerFacility) {
        this.ownerFacility = ownerFacility;
    }
    public String getOwnerFacility() {
        return ownerFacility;
    }

    public void setEventId(String documentId) {
    }

    public String getAddress() {
        return eventAddress;
    }
    public void setAddress(String address) {
        this.eventAddress = address;
    }
}
