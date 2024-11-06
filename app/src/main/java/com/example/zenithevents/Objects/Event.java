package com.example.zenithevents.Objects;


import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Event implements Serializable {
    private ArrayList<User> waitingList, selectedList, cancelledList, confirmedList;
    private String deviceId, eventId, eventName, QRCodeURL, eventImage;
    private int eventLimit;

    public Event(){
        this.waitingList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.confirmedList = new ArrayList<>();

        this.eventName = null;
        this.eventImage = null;
        this.QRCodeURL = null;

        this.eventLimit = 0;
    }

    public Event(String eventName, String eventImage, String QRCodeURL, int eventLimit){
        this.waitingList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.confirmedList = new ArrayList<>();

        this.eventName = eventName;
        this.eventImage = eventImage;
        this.QRCodeURL = QRCodeURL;

        this.eventLimit = eventLimit;
    }

    public ArrayList<User> getWaitingList() {
        return this.waitingList;
    }

    public ArrayList<User> getSelectedList() {
        return this.selectedList;
    }

    public ArrayList<User> getCancelledList() {
        return this.cancelledList;
    }

    public ArrayList<User> getConfirmedList() {
        return this.confirmedList;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return this.eventName;
    }

    public int getEventLimit() {
        return this.eventLimit;
    }

    public String getQRCodeURL() {
        return this.QRCodeURL;
    }

    public String getEventImage() {
        return this.eventImage;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setWaitingList(ArrayList<User> waitingList) {
        this.waitingList = waitingList;
    }

    public void setSelectedList(ArrayList<User> selectedList) {
        this.selectedList = selectedList;
    }

    public void setCancelledList(ArrayList<User> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public void setConfirmedList(ArrayList<User> confirmedList) {
        this.confirmedList = confirmedList;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventLimit(int eventLimit) {
        this.eventLimit = eventLimit;
    }

    public void setQRCodeURL(String QRCodeURL) {
        this.QRCodeURL = QRCodeURL;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public ArrayList<User> drawLottery(ArrayList<User> waitingList, int sampleSize) {
        Collections.shuffle(waitingList);
        int sampleSizeUpdated = Math.min(sampleSize, waitingList.size());

        ArrayList<User> selectedList = new ArrayList<>(waitingList.subList(0, sampleSizeUpdated));
        return selectedList;
    }

    public void sendNotifications(String message, ArrayList<User> recipients){
        // Send notifications to recipients
        for (User recipient : recipients) {
            if (recipient.wantsNotifs()){
            recipient.sendNotification(message);}
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
