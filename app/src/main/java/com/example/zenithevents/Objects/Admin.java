package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Admin {
    private String adminId;
    private String adminFirstName;
    private String adminLastName;
    private String email;
    private String phoneNumber;
    private ArrayList<Event> events;
    private ArrayList<Entrant> entrants;
    private ArrayList<Facility> facilities;

    public Admin(String adminId, String adminFirstName, String adminLastName, String email, String phoneNumber) {
        this.adminId = adminId;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.events = new ArrayList<>();
        this.entrants = new ArrayList<>();
        this.facilities = new ArrayList<>();
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }
    public void removeEntrant(Entrant entrant) {
        entrants.remove(entrant);
    }
    public void removeFacility(Facility facility) {
        facilities.remove(facility);
    }
    public void removeProfilePicture(Entrant entrant) {
        entrant.setProfileImageURL("");

    }
    public void removeEventImage(Event event) {
        event.setEventImage("");

    }
    public void removeQRCode(Event event) {
        event.setQRCodeURL("");
    }

}
