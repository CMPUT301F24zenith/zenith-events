package com.example.zenithevents.Objects;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Events.CreationSuccessActivity;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.UserUtils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents an event in the system.
 * The event includes information such as the event ID, title, description,
 * participants (waiting list, selected participants, registrants), and more.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class Event implements Serializable {
    private ArrayList<String> waitingList, selected, cancelledList, registrants;
    private String ownerFacility, eventId, eventTitle, QRCodeHash, QRCodeBitmap, ImageUrl, eventAddress, eventDescription;
    private Boolean hasGeolocation;
    private int numParticipants, selectedLimit;
    private EventUtils eventUtils;
    private UserUtils userUtils;

    /**
     * Default constructor that initializes all lists and fields to default values.
     * The lists are initialized as empty, and the string fields are initialized as null.
     * The participant-related fields are set to 0.
     * <p>Note: The Javadocs for this constructor were generated with the assistance of an AI language model.</p>
     */
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
        this.hasGeolocation = false;

        this.numParticipants = 0;
        this.selectedLimit = 0;
        this.eventUtils = new EventUtils();
        this.userUtils = new UserUtils();
    }

    /**
     * Constructor to initialize an event with specific values such as event ID, title, image, and QR code details.
     * This constructor is used to create an event with the provided parameters.
     *
     * @param eventId         The unique identifier for the event.
     * @param eventTitle      The title of the event.
     * @param eventImage      The URL of the image associated with the event.
     * @param QRCodeHash      The hash of the event's QR code.
     * @param QRCodeBitmap    The bitmap representation of the event's QR code.
     * @param numParticipants The number of participants allowed in the event.
     * @param eventAddress    The address where the event is taking place.
     * @param hasGeolocation  Whether the event will track geolocation
     * <p>Note: The Javadocs for this constructor were generated with the assistance of an AI language model.</p>
     */
    public Event(String eventId, String eventTitle, String eventImage, String QRCodeHash, String QRCodeBitmap, int numParticipants, String eventAddress, Boolean hasGeolocation){
        this.waitingList = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.cancelledList = new ArrayList<>();
        this.registrants = new ArrayList<>();

        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.ImageUrl = eventImage;
        this.QRCodeHash = QRCodeHash;
        this.QRCodeBitmap = QRCodeBitmap;
        this.hasGeolocation = hasGeolocation;

        this.numParticipants = numParticipants;
        this.eventAddress = eventAddress;
    }

    /**
     * Gets the address of the event.
     *
     * @return The event address.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getEventAddress() {
        return eventAddress;
    }

    /**
     * Sets the address of the event.
     *
     * @param eventAddress The address to set for the event.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    /**
     * Gets the list of participants on the waiting list for the event.
     *
     * @return The waiting list of participants.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public ArrayList<String> getWaitingList() {
        return this.waitingList;
    }

    /**
     * Gets the list of selected participants to register for the event.
     *
     * @return The list of selected participants.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public ArrayList<String> getSelected() {
        return this.selected;
    }

    /**
     * Gets the list of participants who have cancelled their invitation for the event.
     *
     * @return The list of cancelled participants.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public ArrayList<String> getCancelledList() {
        return this.cancelledList;
    }

    /**
     * Gets the list of registrants for the event.
     *
     * @return The list of registrants.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public ArrayList<String> getRegistrants() {
        return this.registrants;
    }

    /**
     * Gets the unique identifier for the event.
     *
     * @return The event ID.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Gets the title of the event.
     *
     * @return The event title.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getEventTitle() {
        return this.eventTitle;
    }

    /**
     * Gets the maximum number of participants allowed for the event.
     *
     * @return The number of participants.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public int getNumParticipants() {
        return this.numParticipants;
    }

    /**
     * Gets the hash of the event's QR code.
     *
     * @return The QR code hash.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getQRCodeHash() {
        return this.QRCodeHash;
    }

    /**
     * Gets the bitmap representation of the event's QR code.
     *
     * @return The QR code bitmap.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getQRCodeBitmap() { return this.QRCodeBitmap; }

    /**
     * Gets the image bitmap associated with the event.
     *
     * @return The image bitmap.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getImageUrl() {
        return this.ImageUrl;
    }

    /**
     * Sets the unique identifier for the event.
     *
     * @param eventId The event ID to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Sets the waiting list of participants.
     *
     * @param waitingList The waiting list to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setWaitingList(ArrayList<String> waitingList) {
        this.waitingList = waitingList;
    }

    /**
     * Sets the list of selected participants.
     *
     * @param selected The selected participants to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }

    /**
     * Sets the registrants for the event.
     *
     * @param registrants The list of registrants to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setRegistrants(ArrayList<String> registrants) {
        this.registrants = registrants;
    }

    /**
     * Sets the list of participants who have cancelled their registration for the event.
     *
     * @param cancelledList The cancelled participants list to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    /**
     * Sets the title of the event.
     *
     * @param eventTitle The title to set for the event.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Sets the maximum number of participants allowed for the event.
     *
     * @param numParticipants The number of participants to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    /**
     * Sets the hash of the event's QR code.
     *
     * @param QRCodeHash The QR code hash to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setQRCodeHash(String QRCodeHash) {
        this.QRCodeHash = QRCodeHash;
    }

    /**
     * Sets the bitmap representation of the event's QR code.
     *
     * @param QRCodeBitmap The QR code bitmap to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setQRCodeBitmap(String QRCodeBitmap) { this.QRCodeBitmap = QRCodeBitmap; }

    /**
     * Sets the image URL associated with the event.
     *
     * @param eventImage The image URL to set for the event.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setImageUrl(String eventImage) {
        this.ImageUrl = eventImage;
    }

    /**
     * Draws a lottery from the waiting list of an event and selects
     * a sample of participants. The sample size is constrained to
     * the size of the selected list.
     */
    public void drawLottery() {
        ArrayList<String> sampledList = new ArrayList<>();
        ArrayList<String> newSelectedList = this.getSelected();
        ArrayList<String> waitingList = this.getWaitingList();
        ArrayList<String> newWaitingList = this.getWaitingList();
        Collections.shuffle(waitingList);

        if (this.getSelectedLimit() == 0) {
            sampledList.addAll(waitingList);
        } else {
            int sampleSizeUpdated = this.getSelectedLimit() - this.getSelected().size() - this.getRegistrants().size();
            sampleSizeUpdated = Math.min(sampleSizeUpdated, this.getWaitingList().size());
            waitingList = new ArrayList<>(waitingList.subList(0, sampleSizeUpdated));
            sampledList.addAll(waitingList);
        }
        newSelectedList.addAll(sampledList);
        newWaitingList.removeAll(sampledList);

        this.setSelected(newSelectedList);
        this.setWaitingList(newWaitingList);

        ArrayList<String> finalWaitingList = waitingList;
        this.eventUtils.createUpdateEvent(this, eventId -> {
            finalWaitingList.forEach(deviceId -> {
                this.userUtils.fetchUserProfile(deviceId, callback -> {
                    if (callback != null) {
                        ArrayList<String> waitingEvents = callback.getWaitingEvents();
                        waitingEvents.remove(this.eventId);
                        callback.setWaitingEvents(waitingEvents);

                        ArrayList<String> selectedEvents = callback.getSelectedEvents();
                        selectedEvents.add(this.eventId);
                        callback.setSelectedEvents(selectedEvents);

                        this.userUtils.updateUserById(callback, callback2 -> {
                            Log.d("Firestore", "User: " + deviceId + "info updated.");
                        });
                    }
                });
            });
        });

        Log.d("FunctionCall", "4-- " + this.getSelected().size());
        Log.d("FunctionCall", "5-- " + this.getWaitingList().size());
    }

//    public void sendNotifications(String message, ArrayList<String> recipients){
//        // Send notifications to recipients
//        for (String recipient : recipients) {
//            if (recipient.wantsNotifs()){
//            recipient.sendNotification(message);}
//        }
//    }

    /**
     * Gets the owner facility of the event.
     *
     * @return The owner facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getOwnerFacility() {
        return ownerFacility;
    }

    /**
     * Sets the owner facility of the event.
     *
     * @param ownerFacility The owner facility to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setOwnerFacility(String ownerFacility) {
        this.ownerFacility = ownerFacility;
    }

    /**
     * Gets the description of the event.
     *
     * @return The event description.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the description of the event.
     *
     * @param eventDescription The event description to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the selected limit for the event, which constrains the number of selected participants.
     *
     * @return The selected limit for the event.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public int getSelectedLimit() {
        return selectedLimit;
    }

    /**
     * Gets the selected limit for the event, which constrains the number of selected participants.
     *
     * @return The selected limit for the event.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setSelectedLimit(int selectedLimit) {
        this.selectedLimit = selectedLimit;
    }

    public Boolean getHasGeolocation() {
        return hasGeolocation;
    }

    public void setHasGeolocation(Boolean hasGeolocation) {
        this.hasGeolocation = hasGeolocation;
    }
}
