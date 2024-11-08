package com.example.zenithevents;

import com.example.zenithevents.Objects.Facility;

import org.junit.Test;

public class FacilityObjectTest {
    @Test
    public void testFacilityObject() {
        Facility facility = new Facility("Test Facility", "123 Test Street");
        assert facility.getFacilityName().equals("Test Facility");
        assert facility.getAddress().equals("123 Test Street");
    }
}
