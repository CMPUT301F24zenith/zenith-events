package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.Event;

import org.junit.Before;
import org.junit.Test;

public class CreateEventQrcodeTest {
    private Event event;

    @Before
    public void setUp() {
        event = new Event();
    }
    @Test
    public void testCreateEventQrcode() {
        event.setEventTitle("Test Event");
        event.setEventDescription("Test Description");
        event.setImageUrl("Test Image URL");

    String qrCode = "Test Newly generated QR Code";
    event.setQRCodeHash(qrCode);
    assertEquals(qrCode, event.getQRCodeHash());
    }
}
