package com.example.zenithevents.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.EntrantDashboard.EventFragment;
import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;

public class OrganizerPage extends AppCompatActivity {
    Button createEventButton;
    ArrayList<Event> organizerEvents;
    EventUtils eventUtils = new EventUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main);
        Context context = OrganizerPage.this;
        String deviceID = DeviceUtils.getDeviceID(context);

        eventUtils.fetchOrganizerEvents(context, deviceID, fetchedOrganizerEvents -> {
            if (fetchedOrganizerEvents != null)
                organizerEvents = fetchedOrganizerEvents;

            Log.d("FunctionCall", "Fetched events: " + organizerEvents.size());
        });

        createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(v -> {
            Event event = new Event();
            Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
            intent.putExtra("page_title", "Create Event");
            intent.putExtra("Event", event);
            startActivity(intent);
        });

        if (savedInstanceState == null) {
            EventFragment myEvents = new EventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.myEventsFragment, myEvents)
                    .commit();
        }
    }
}
