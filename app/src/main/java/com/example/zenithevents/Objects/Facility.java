package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Facility {
    private String facilityName;
    private String address;
    private String ownerID;
    private ArrayList<Event> events;


    public Facility(String facilityName, String address) {
        this.facilityName = facilityName;
        this.address = address;
    }

    public String getFacilityName() {
        return facilityName;
    }
    public String getAddress() {
        return address;
    }
}
