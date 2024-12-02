package com.example.zenithevents;



import com.example.zenithevents.Objects.Facility;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class FacilityManagingTest {

    private Facility facility;

    @Before
    public void setUp() {
        // Initialize the Facility object with sample data
        facility = new Facility("Test Facility Name", "1234567890", "test@example.com", "sampleDeviceId");
    }

    @Test
    public void testFacilityName() {
        // Check if the facility name is correctly set and retrieved
        assertEquals("Test Facility Name", facility.getNameOfFacility());
    }

    @Test
    public void testFacilityPhone() {
        // Check if the facility phone number is correctly set and retrieved
        assertEquals("1234567890", facility.getPhoneOfFacility());
    }

    @Test
    public void testFacilityEmail() {
        // Check if the facility email is correctly set and retrieved
        assertEquals("test@example.com", facility.getEmailOfFacility());
    }

    @Test
    public void testFacilityDeviceId() {
        // Check if the facility device ID is correctly set and retrieved
        assertEquals("sampleDeviceId", facility.getDeviceId());
    }

    @Test
    public void testUpdatingFacilityDetails() {
        // Update facility details to simulate editing the facility
        facility.setNameOfFacility("Updated Facility Name");
        facility.setPhoneOfFacility("0987654321");
        facility.setEmailOfFacility("updated@example.com");

        // Verify the updated details
        assertEquals("Updated Facility Name", facility.getNameOfFacility());
        assertEquals("0987654321", facility.getPhoneOfFacility());
        assertEquals("updated@example.com", facility.getEmailOfFacility());
    }


}
