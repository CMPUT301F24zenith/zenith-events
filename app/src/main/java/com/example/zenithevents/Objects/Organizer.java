package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Organizer {
    private String role;
    private String organizerId;
    private String organizerName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> organizerEvents;
    private String profileImageURL;

    public Organizer(String role, String organizerId, String organizerName, String email, String phoneNumber) {
        this.role = role;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public ArrayList<Event> getOrganizerEvents() {
        return organizerEvents;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOrganizerEvents(ArrayList<Event> organizerEvents) {
        this.organizerEvents = organizerEvents;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
