package com.example.zenithevents;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.example.zenithevents.DrawableMatcher.hasDrawable;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static java.util.regex.Pattern.matches;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Objects.Event;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

@RunWith(AndroidJUnit4.class)
public class UploadEventImageTest {
    private Event event;


    @Before
    public void setUp() {

        event = new Event();
        event.setImageUrl("https://example.com/poster.jpg");

    }


    @Test
    public void testEventPosterUpload() {
        assertNotNull(event.getImageUrl());
        event.setImageUrl("https://example.com/new_poster.jpg");
        assertNotNull(event.getImageUrl());
        assertNotEquals("https://example.com/poster.jpg", event.getImageUrl());

    }

}
