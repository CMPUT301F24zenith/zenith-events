package com.example.zenithevents.Objects;

import java.util.ArrayList;

/**
 * Represents a facility with associated contact information and device ID.
 * The facility includes details such as name, phone number, email, and device ID.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class Facility {
    private String nameOfFacility;
    private String phoneOfFacility;
    private String emailOfFacility;
    private String deviceId;   //get deviceid from deviceutils

    /**
     * Default constructor that initializes a facility with no parameters.
     * <p>Note: The Javadocs for this constructor were generated with the assistance of an AI language model.</p>
     */
    public Facility() {}

    /**
     * Constructor to initialize a facility with specific values such as name, phone, email, and device ID.
     *
     * @param nameOfFacility The name of the facility.
     * @param phoneOfFacility The phone number of the facility.
     * @param emailOfFacility The email address of the facility.
     * @param deviceId The unique device ID for the facility.
     * <p>Note: The Javadocs for this constructor were generated with the assistance of an AI language model.</p>
     */
    public Facility(String nameOfFacility, String phoneOfFacility, String emailOfFacility, String deviceId) {
        this.nameOfFacility = nameOfFacility;
        this.phoneOfFacility = phoneOfFacility;
        this.emailOfFacility = emailOfFacility;
        this.deviceId = deviceId;
    }

    /**
     * Gets the name of the facility.
     *
     * @return The name of the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getNameOfFacility() {
        return nameOfFacility;
    }

    /**
     * Sets the name of the facility.
     *
     * @param nameOfFacility The name to set for the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setNameOfFacility(String nameOfFacility) {
        this.nameOfFacility = nameOfFacility;
    }

    /**
     * Gets the phone number of the facility.
     *
     * @return The phone number of the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getPhoneOfFacility() {
        return phoneOfFacility;
    }

    /**
     * Sets the phone number of the facility.
     *
     * @param phoneOfFacility The phone number to set for the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setPhoneOfFacility(String phoneOfFacility) {
        this.phoneOfFacility = phoneOfFacility;
    }

    /**
     * Gets the email address of the facility.
     *
     * @return The email address of the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getEmailOfFacility() {
        return emailOfFacility;
    }

    /**
     * Sets the email address of the facility.
     *
     * @param emailOfFacility The email address to set for the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setEmailOfFacility(String emailOfFacility) {
        this.emailOfFacility = emailOfFacility;
    }

    /**
     * Gets the device ID associated with the facility.
     *
     * @return The device ID of the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the device ID for the facility.
     *
     * @param deviceId The device ID to set for the facility.
     * <p>Note: The Javadocs for this method were generated with the assistance of an AI language model.</p>
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}