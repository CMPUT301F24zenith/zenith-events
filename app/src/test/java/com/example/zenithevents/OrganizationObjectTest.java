package com.example.zenithevents;

import com.example.zenithevents.Objects.Organization;

import org.junit.Test;

public class OrganizationObjectTest {
    @Test
    public void testOrganizationObject() {
        Organization organization = new Organization("Test Organization");
        assert organization.getOrganizationName().equals("Test Organization");
        assert organization.getEvents().isEmpty();
    }
}
