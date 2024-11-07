package com.example.zenithevents.Objects;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Event implements Serializable {
    private ArrayList<User> waitingList, selected, cancelledList, registrants;
    private String ownerFacility, eventId, eventTitle, QRCodeURL, ImageUrl, eventAddress, eventDescription;
    private int numParticipants;

    public Event(){
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.registrants = new ArrayList<>();

        this.eventTitle = null;
        this.ImageUrl = null;
        this.QRCodeURL = null;
        this.eventAddress = null;
        this.eventId = null;

        this.numParticipants = 0;
    }

    public Event(String eventId, String eventTitle, String eventImage, String QRCodeURL, int numParticipants, String eventAddress){
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.registrants = new ArrayList<>();

        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.ImageUrl = eventImage;
        this.QRCodeURL = QRCodeURL;

        this.numParticipants = numParticipants;
        this.eventAddress = eventAddress;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public ArrayList<User> getWaitingList() {
        return this.waitingList;
    }

    public ArrayList<User> getSelected() {
        return this.selected;
    }

    public ArrayList<User> getCancelledList() {
        return this.cancelledList;
    }

    public ArrayList<User> getRegistrants() {
        return this.registrants;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return this.eventTitle;
    }

    public int getNumParticipants() {
        return this.numParticipants;
    }

    public String getQRCodeURL() {
        return this.QRCodeURL;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setWaitingList(ArrayList<User> waitingList) {
        this.waitingList = waitingList;
    }

    public void setSelected(ArrayList<User> selected) {
        this.selected = selected;
    }

    public void setRegistrants(ArrayList<User> registrants) {
        this.registrants = registrants;
    }

    public void setCancelledList(ArrayList<User> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setNumParticipants(int eventLimit) {
        this.numParticipants = eventLimit;
    }

    public void setQRCodeURL(String QRCodeURL) {
        this.QRCodeURL = QRCodeURL;
    }

    public void setImageUrl(String eventImage) {
        this.ImageUrl = eventImage;
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

    public String getOwnerFacility() {
        return ownerFacility;
    }

    public void setOwnerFacility(String ownerFacility) {
        this.ownerFacility = ownerFacility;
    }

    public String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
