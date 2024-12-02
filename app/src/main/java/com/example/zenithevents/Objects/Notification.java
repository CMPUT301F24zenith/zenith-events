package com.example.zenithevents.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a notification for a user containing messages, event details, and subjects.
 * This class implements Serializable for object serialization.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class Notification implements Serializable {
    String userId;
    private ArrayList<String> messages, eventIds, subjects, eventNames;

    /**
     * Constructor to initialize a Notification with a userId and empty lists.
     *
     * @param userId The ID of the user receiving the notification.
     */
    public Notification(String userId){
        this.messages = new ArrayList<>();
        this.eventIds = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.eventNames = new ArrayList<>();
    }

    /**
     * Constructor to initialize a Notification with userId, messages, event names,
     * event IDs, and subjects.
     *
     * @param userId     The ID of the user receiving the notification.
     * @param messages   A list of messages to be sent with the notification.
     * @param eventNames A list of event names associated with the notification.
     * @param eventIds   A list of event IDs associated with the notification.
     * @param subjects   A list of subjects related to the notification.
     */
    public Notification(String userId, ArrayList<String> messages, ArrayList<String> eventNames, ArrayList<String> eventIds, ArrayList<String> subjects){
        this.userId = userId;
        this.messages = messages;
        this.eventIds = eventIds;
        this.subjects = subjects;
        this.eventNames = subjects;
    }

    /**
     * Gets the list of messages associated with the notification.
     *
     * @return A list of messages.
     */
    public ArrayList<String> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages for the notification.
     *
     * @param messages A list of messages to set.
     */
    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    /**
     * Gets the list of event IDs associated with the notification.
     *
     * @return A list of event IDs.
     */
    public ArrayList<String> getEventIds() {
        return eventIds;
    }

    /**
     * Sets the list of event IDs for the notification.
     *
     * @param eventIds A list of event IDs to set.
     */
    public void setEventIds(ArrayList<String> eventIds) {
        this.eventIds = eventIds;
    }

    /**
     * Gets the list of subjects associated with the notification.
     *
     * @return A list of subjects.
     */
    public ArrayList<String> getSubjects() {
        return subjects;
    }

    /**
     * Sets the list of subjects for the notification.
     *
     * @param subjects A list of subjects to set.
     */
    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    /**
     * Gets the list of event names associated with the notification.
     *
     * @return A list of event names.
     */
    public ArrayList<String> getEventNames() {
        return eventNames;
    }

    /**
     * Sets the list of event names for the notification.
     *
     * @param eventNames A list of event names to set.
     */
    public void setEventNames(ArrayList<String> eventNames) {
        this.eventNames = eventNames;
    }
}