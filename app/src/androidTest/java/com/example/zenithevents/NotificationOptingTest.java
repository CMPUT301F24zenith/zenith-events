package com.example.zenithevents;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.CheckBox;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Objects.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


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
