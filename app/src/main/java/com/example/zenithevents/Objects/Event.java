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
    private ArrayList<String> waitingList, selected, cancelledList, registrants;
    private String ownerFacility, eventId, eventTitle, QRCodeHash, QRCodeBitmap, ImageUrl, eventAddress, eventDescription;
    private int numParticipants, selectedLimit;

    public Event(){
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.registrants = new ArrayList<>();

        this.eventTitle = null;
        this.ImageUrl = null;
        this.QRCodeHash = null;
        this.QRCodeBitmap = null;
        this.eventAddress = null;
        this.eventId = null;

        this.numParticipants = 0;
        this.selectedLimit = 0;
    }

    public Event(String eventId, String eventTitle, String eventImage, String QRCodeHash, String QRCodeBitmap, int numParticipants, String eventAddress){
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.registrants = new ArrayList<>();

        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.ImageUrl = eventImage;
        this.QRCodeHash = QRCodeHash;
        this.QRCodeBitmap = QRCodeBitmap;

        this.numParticipants = numParticipants;
        this.eventAddress = eventAddress;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public ArrayList<String> getWaitingList() {
        return this.waitingList;
    }

    public ArrayList<String> getSelected() {
        return this.selected;
    }

    public ArrayList<String> getCancelledList() {
        return this.cancelledList;
    }

    public ArrayList<String> getRegistrants() {
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

    public String getQRCodeHash() {
        return this.QRCodeHash;
    }

    public String getQRCodeBitmap() { return this.QRCodeBitmap; }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    public void setQRCodeHash(String QRCodeHash) {
        this.QRCodeHash = QRCodeHash;
    }

    public void setQRCodeBitmap(String QRCodeBitmap) { this.QRCodeBitmap = QRCodeBitmap; }

    public void setImageUrl(String eventImage) {
        this.ImageUrl = eventImage;
    }

    public ArrayList<String> drawLottery(ArrayList<String> waitingList, int sampleSize) {
        Collections.shuffle(waitingList);
        int sampleSizeUpdated = Math.min(sampleSize, waitingList.size());

        ArrayList<String> selectedList = new ArrayList<>(waitingList.subList(0, sampleSizeUpdated));
        return selectedList;
    }

//    public void sendNotifications(String message, ArrayList<String> recipients){
//        // Send notifications to recipients
//        for (String recipient : recipients) {
//            if (recipient.wantsNotifs()){
//            recipient.sendNotification(message);}
//        }
//    }

    public String getOwnerFacility() {
        return ownerFacility;
    }

    public void setOwnerFacility(String ownerFacility) {
        this.ownerFacility = ownerFacility;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getSelectedLimit() {
        return selectedLimit;
    }

    public void setSelectedLimit(int selectedLimit) {
        this.selectedLimit = selectedLimit;
    }
}
