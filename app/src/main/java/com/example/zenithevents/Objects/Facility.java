package com.example.zenithevents.Objects;

public class Facility {
    private String facilityName;
    private String address;


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
