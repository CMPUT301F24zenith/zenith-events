package com.example.zenithevents;


import com.example.zenithevents.Objects.Organizer;

import org.junit.Test;

public class OrganizerObjectTest {
    @Test
    public void testOrganizerObject() {

        Organizer organizer = new Organizer("Organizer","329843", "John Doe", "john@example.com", "123-456-7890");

        assert organizer.getOrganizerName().equals("John Doe");
        assert organizer.getEmail().equals("john@example.com");
        assert organizer.getPhoneNumber().equals("123-456-7890");
        assert organizer.getRole().equals("Organizer");
    }

}
