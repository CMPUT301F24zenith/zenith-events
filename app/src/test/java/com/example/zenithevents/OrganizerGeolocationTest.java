package com.example.zenithevents;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

import android.widget.CheckBox;

import com.example.zenithevents.Objects.Event;

import org.junit.Test;

public class OrganizerGeolocationTest {

    @Test
    public void testOrganizerOptInGeolocation() {
        Event event = new Event();
        event.setHasGeolocation(true);
        CheckBox checkbox = new CheckBox(getContext());
        checkbox.setChecked(event.getHasGeolocation());
        assertEquals(true, event.getHasGeolocation());
    }

    @Test
    public void testOrganizerOptOutGeolocation() {
        Event event = new Event();
        event.setHasGeolocation(false);
        CheckBox checkbox = new CheckBox(getContext());
        checkbox.setChecked(event.getHasGeolocation());
        assertEquals(false, event.getHasGeolocation());
    }

}
