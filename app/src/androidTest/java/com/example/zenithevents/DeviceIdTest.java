package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.User;

import org.junit.Before;
import org.junit.Test;

public class DeviceIdTest {
    private User user;
    @Before
    public void setUp() {
        user = new User();
        user.setDeviceID("testDeviceId");
    }

    @Test
    public void testGetDeviceId() {
        String deviceId = user.getDeviceID();
        assertEquals("testDeviceId", deviceId);
    }


}
