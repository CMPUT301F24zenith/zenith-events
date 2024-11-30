package com.example.zenithevents.Objects;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Object> userLocations;

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
        this.userLocations = new HashMap<>();
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
    public Event(String eventId, String eventTitle, String eventImage, String QRCodeHash, String QRCodeBitmap, int numParticipants, String eventAddress, Boolean hasGeolocation, Map<String, Object> userLocations){
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
        this.userLocations = userLocations;
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
        this.waitingList = new ArrayList<>();
        this.waitingList.addAll(waitingList);
    }

    /**
     * Sets the list of selected participants.
     *
     * @param selected The selected participants to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setSelected(ArrayList<String> selected) {
        this.selected = new ArrayList<>();
        this.selected.addAll(selected);
    }

    /**
     * Sets the registrants for the event.
     *
     * @param registrants The list of registrants to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setRegistrants(ArrayList<String> registrants) {
        this.registrants = new ArrayList<>();
        this.registrants.addAll(registrants);
    }

    /**
     * Sets the list of participants who have cancelled their registration for the event.
     *
     * @param cancelledList The cancelled participants list to set.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = new ArrayList<>();
        this.cancelledList.addAll(cancelledList);
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
    public ArrayList<String> drawLottery(Context context) {
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

        EventUtils eventUtils = new EventUtils();
        UserUtils userUtils = new UserUtils();

        eventUtils.createUpdateEvent(this, eventId -> {
            for (String deviceId : this.getSelected()) {
                userUtils.fetchUserProfile(deviceId, callback -> {
                    callback.getWaitingEvents().remove(this.eventId);

                    ArrayList<String> selectedEvents = callback.getSelectedEvents();
                    if (!selectedEvents.contains(this.eventId))
                        selectedEvents.add(this.eventId);
                    callback.setSelectedEvents(selectedEvents);

                    if (sampledList.contains(deviceId) && callback.getWantsNotifs()) {
                        Log.d("FunctionCall", "wants notifs");
                        callback.sendNotification(context, "Congratulations! You have been selected for " + this.getEventTitle());
                    }
                    userUtils.updateUserByObject(callback, callback2 -> {
                        Log.d("FunctionCall", "User: " + deviceId + "info updated.");
                    });
                });
            }
            for (String deviceId : this.getWaitingList()) {
                userUtils.fetchUserProfile(deviceId, callback -> {
                    if (callback.getWantsNotifs()) {
                        callback.sendNotification(context, "You have not been selected for " + this.getEventTitle());
                        userUtils.updateUserByObject(callback, callback2 -> {
                            Log.d("Notification", "User: " + deviceId + "notified.");
                        });
                    }
                });
            }
        });

        return this.getSelected();
    }

    /**
     * Sends notifications to participants who enabled notifications.
     *
     * @param entrants The list of participants.
     * @param message  The message to send to participants who enabled notifications.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void sendNotifications(Context context,String message, ArrayList<String> entrants){
//         Send notifications to recipients
        UserUtils userUtils = new UserUtils();
        try {
            for (String deviceID : entrants) {
                Log.d("FunctionCall", "Profile fetched for: " + deviceID);
                userUtils.fetchUserProfile(deviceID, user -> {
                    Log.d("FunctionCall", "Profile fetched for: " + user.getDeviceID());
                    if (user.getWantsNotifs()) {
                        user.sendNotification(context, message);
                        userUtils.updateUserByObject(user, user2 -> {
                            Log.d("FunctionCall", "Notification sent to: " + deviceID);
                        });
                    }
                });
            }
            Toast.makeText(context, "Notifications sent successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error sending notifications: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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

    public Map<String, Object> getUserLocations() {
        return userLocations;
    }

    public void setUserLocations(Map<String, Object> userLocations) {
        this.userLocations = userLocations;
    }

    public void updateUserLocation(String userId, double latitude, double longitude) {
        Log.d("FunctionCall", "updating location...");
        if (userLocations != null) {
            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("latitude", latitude);
            locationMap.put("longitude", longitude);
            userLocations.put(userId, locationMap);
        }
    }
}
