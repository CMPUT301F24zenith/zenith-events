package com.example.zenithevents;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

import android.widget.CheckBox;

import com.example.zenithevents.Objects.User;

import org.junit.Test;


public class NotificationOptingTest {



    @Test
    public void OptInTest() {
        User newUser = new User("testDeviceID", "testFirstName", "testLastName", "testEmail", "testPhoneNumber");
        newUser.setWantsNotifs(true);
        CheckBox notifsCheckBox = new CheckBox(getContext());
        notifsCheckBox.setChecked(newUser.getWantsNotifs());
        assertEquals(newUser.getWantsNotifs(), notifsCheckBox.isChecked());

    }
    @Test
    public void OptOutTest(){
        User newUser = new User("testDeviceID", "testFirstName", "testLastName", "testEmail", "testPhoneNumber");
        newUser.setWantsNotifs(false);
        CheckBox notifsCheckBox = new CheckBox(getContext());
        notifsCheckBox.setChecked(newUser.getWantsNotifs());
        assertEquals(newUser.getWantsNotifs(), notifsCheckBox.isChecked());
    }

}
