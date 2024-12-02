package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;

public class QrCodeHashTest {
    @Test
    public void testRemoveQrCodeHash() {
        Event event = new Event();
        event.setQRCodeHash("testHash");
        assertEquals("testHash", event.getQRCodeHash());
        event.setQRCodeHash(null);
        assertEquals(null, event.getQRCodeHash());
    }
}
