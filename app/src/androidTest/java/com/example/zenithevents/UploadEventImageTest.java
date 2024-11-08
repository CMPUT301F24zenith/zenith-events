package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

import com.example.zenithevents.Objects.Event;

import org.junit.Before;
import org.junit.Test;

public class UploadEventImageTest {
    private Event event;

    @Before
    public void setUp() {
        event = new Event();
    }
    @Test
    public void testUploadEventImage() {
        String imageUrl = "https://example.com/event_image.jpg";
        event.setImageUrl(imageUrl);
        assertEquals(imageUrl, event.getImageUrl());
    }


}
