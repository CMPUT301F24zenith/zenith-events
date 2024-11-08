package com.example.zenithevents.Objects;

import java.util.ArrayList;

public class Facility {
    private String nameOfFacility;
    private String phoneOfFacility;
    private String emailOfFacility;
    private String deviceId;   //get deviceid from deviceutils
    public Facility() {}

    public Facility(String nameOfFacility, String phoneOfFacility, String emailOfFacility, String deviceId) {
        this.nameOfFacility = nameOfFacility;
        this.phoneOfFacility = phoneOfFacility;
        this.emailOfFacility = emailOfFacility;
        this.deviceId = deviceId;
    }

    public String getNameOfFacility() {
        return nameOfFacility;
    }

    public void setNameOfFacility(String nameOfFacility) {
        this.nameOfFacility = nameOfFacility;
    }

    public String getPhoneOfFacility() {
        return phoneOfFacility;
    }

    public void setPhoneOfFacility(String phoneOfFacility) {
        this.phoneOfFacility = phoneOfFacility;
    }

    public String getEmailOfFacility() {
        return emailOfFacility;
    }

    public void setEmailOfFacility(String emailOfFacility) {
        this.emailOfFacility = emailOfFacility;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}