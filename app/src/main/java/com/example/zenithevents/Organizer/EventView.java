package com.example.zenithevents.Organizer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

public class EventView extends AppCompatActivity {
    // create a TAG
    private static final String TAG = "EventView";
    ImageView eventPosterimageView;
    Button btnJoinWaitingList, btnLeaveWaitingList;
    TextView QRCodeRequiredText, eventName, facilityName, address;
    ProgressBar progressBar;
    EventUtils eventUtils = new EventUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Retrieve the event ID from the Intent
        String eventId = getIntent().getStringExtra("event_id");


        eventPosterimageView = findViewById(R.id.eventImage);
        btnJoinWaitingList = findViewById(R.id.btnJoinWaitingList);
        btnLeaveWaitingList = findViewById(R.id.btnLeaveWaitingList);
        QRCodeRequiredText = findViewById(R.id.QRCodeRequiredText);
        facilityName = findViewById(R.id.facilityName);
        address = findViewById(R.id.address);
        progressBar = findViewById(R.id.progressBar);
        eventName = findViewById(R.id.eventName);

        eventUtils.fetchEventById(eventId, new EventUtils.EventFetchCallback() {
            public void onEventFetchComplete(Event event) {
                if (event != null) {
                    String title = event.getTitle();
                    String imageUrl = event.getImageUrl();
                    String facility = event.getOwnerFacility();
                    //TODO get facility address from database


                }
            }
        });

    }

    private void fetchAndDisplayEventDetails(String eventId) {
        if (eventId == null) {
            Log.e(TAG, "Event ID not provided in the Intent.");
            return;
        }

        // Show progress bar while loading
        progressBar.setVisibility(ProgressBar.VISIBLE);

        eventUtils.fetchEventById(eventId, new EventUtils.EventFetchCallback() {
            @Override
            public void onEventFetchComplete(Event event) {
                if (event != null) {
//                    dregenereisplayEventDetails(event);
                } else {
                    Log.e(TAG, "Event not found or an error occurred while fetching.");
                }

                // Hide progress bar after loading
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }
}