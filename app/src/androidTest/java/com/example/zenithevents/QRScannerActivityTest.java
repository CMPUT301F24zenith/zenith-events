package com.example.zenithevents;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.zenithevents.Organizer.EventView;
import com.example.zenithevents.QRCodes.QRScannerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRScannerActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);


    @Before
    public void setUp() {

        Intents.init();

    }
    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testQRScannerButton() {
        Espresso.onView(ViewMatchers.withId(R.id.scanQRButton)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(QRScannerActivity.class.getName()));
    }
    @Test
    public void testInitializeScanner() {
        ActivityScenario<QRScannerActivity> scenario = ActivityScenario.launch(QRScannerActivity.class);
        Espresso.onView(withId(R.id.barcodeView)).perform(click());
//        Intents.intended(IntentMatchers.hasComponent(EventView.class.getName()));
    }

}
