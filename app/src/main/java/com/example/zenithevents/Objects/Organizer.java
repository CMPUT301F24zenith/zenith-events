package com.example.zenithevents.Objects;

import java.util.ArrayList;

/**
 * Represents an event organizer with information such as role, name, email, phone number, and associated events.
 *
 * <p>This class provides methods to manage and retrieve the details of an event organizer, including their role,
 * unique ID, name, contact information, profile image URL, and the events they organize.
 *
 * <p><strong>AI-Generated Documentation:</strong> The Javadocs for this class were generated
 * with assistance from a generative AI language model. The initial documentation drafts were based on
 * the code structure, including method descriptions, parameter details, and functionality explanations.
 * These drafts were then reviewed and refined to ensure clarity and accuracy, with adjustments
 * made for context and readability.
 *
 * <p>This AI-generated documentation aims to enhance code readability, reduce development time, and provide
 * a clear understanding of each method’s purpose and usage within the class.
 */
public class Organizer {
    private String role;
    private String organizerId;
    private String organizerName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> organizerEvents;
    private String profileImageURL;

    /**
     * Constructs an Organizer object with the specified details.
     *
     * @param role          The role of the organizer within the event (e.g., coordinator, manager).
     * @param organizerId   A unique identifier for the organizer.
     * @param organizerName The name of the organizer.
     * @param email         The email address of the organizer.
     * @param phoneNumber   The phone number of the organizer.
     */
    public Organizer(String role, String organizerId, String organizerName, String email, String phoneNumber) {
        this.role = role;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the profile image URL of the organizer.
     *
     * @return A string representing the URL of the organizer's profile image.
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * Gets the list of events associated with the organizer.
     *
     * @return An ArrayList of Event objects that the organizer is managing or organizing.
     */
    public ArrayList<Event> getOrganizerEvents() {
        return organizerEvents;
    }

    /**
     * Gets the phone number of the organizer.
     *
     * @return A string representing the organizer's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the email address of the organizer.
     *
     * @return A string representing the organizer's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the name of the organizer.
     *
     * @return A string representing the organizer's name.
     */
    public String getOrganizerName() {
        return organizerName;
    }

    /**
     * Gets the unique ID of the organizer.
     *
     * @return A string representing the organizer's unique ID.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Gets the role of the organizer within the event.
     *
     * @return A string representing the organizer's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the organizer.
     *
     * @param role A string representing the role of the organizer within the event.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the unique ID of the organizer.
     *
     * @param organizerId A string representing the unique identifier for the organizer.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Sets the name of the organizer.
     *
     * @param organizerName A string representing the name of the organizer.
     */
    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    /**
     * Sets the email address of the organizer.
     *
     * @param email A string representing the organizer's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phone number of the organizer.
     *
     * @param phoneNumber A string representing the organizer's phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the list of events associated with the organizer.
     *
     * @param organizerEvents An ArrayList of Event objects that the organizer is managing or organizing.
     */
    public void setOrganizerEvents(ArrayList<Event> organizerEvents) {
        this.organizerEvents = organizerEvents;
    }

    /**
     * Sets the profile image URL of the organizer.
     *
     * @param profileImageURL A string representing the URL of the organizer's profile image.
     */
    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
