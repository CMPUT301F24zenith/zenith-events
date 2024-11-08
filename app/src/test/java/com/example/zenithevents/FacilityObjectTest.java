package com.example.zenithevents;

import com.example.zenithevents.Objects.Facility;

import org.junit.Test;

public class FacilityObjectTest {
    @Test
    public void testFacilityObject() {

        Facility facility = new Facility("Test Facility", "542 232 1232", "johndoe@gmail.com", "231123AVC");
        assert facility.getNameOfFacility().equals("Test Facility");
        assert facility.getPhoneOfFacility().equals("542 232 1232");
        assert facility.getEmailOfFacility().equals("johndoe@gmail.com");
        assert facility.getDeviceId().equals("231123AVC");
    }
}
