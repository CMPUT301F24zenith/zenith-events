package com.example.zenithevents;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Objects.Event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateEventImageTest {
    private Event event;


    @Before
    public void setUp() {

        event = new Event();
        event.setImageUrl("https://example.com/poster.jpg");

    }


    @Test
    public void testEventPosterUpdate() {
        assertNotNull(event.getImageUrl());
        event.setImageUrl("https://example.com/new_poster.jpg");
        assertNotNull(event.getImageUrl());
        assertNotEquals("https://example.com/poster.jpg", event.getImageUrl());

    }

}
