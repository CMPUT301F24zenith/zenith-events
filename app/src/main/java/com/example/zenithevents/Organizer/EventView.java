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

import com.bumptech.glide.Glide;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class EventView extends AppCompatActivity {

    private static final String TAG = "EventView";


    ImageView eventPosterimageView;
    private Button btnJoinWaitingList, btnLeaveWaitingList;
    private TextView QRCodeRequiredText, eventName, facilityName, address;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration eventListener;

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

        initializeViews();
        setupRealTimeEventListener(eventId);

    }


    private void initializeViews() {
        eventPosterimageView = findViewById(R.id.eventImage);
        btnJoinWaitingList = findViewById(R.id.btnJoinWaitingList);
        btnLeaveWaitingList = findViewById(R.id.btnLeaveWaitingList);
        QRCodeRequiredText = findViewById(R.id.QRCodeRequiredText);
        facilityName = findViewById(R.id.facilityName);
        address = findViewById(R.id.address);
        progressBar = findViewById(R.id.progressBar);
        eventName = findViewById(R.id.eventName);
    }

    private void setupRealTimeEventListener(String eventId) {
        if (eventId == null) {
            Log.e(TAG, "Event ID not provided in the Intent.");
            return;
        }

        // Show progress bar while loading
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Set up the real-time listener for the event document
        eventListener = db.collection("events").document(eventId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listen failed.", e);
                        progressBar.setVisibility(ProgressBar.GONE);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Convert document snapshot to Event object
                        Event event = documentSnapshot.toObject(Event.class);
                        if (event != null) {
                            displayEventDetails(event);
                        }
                    } else {
                        Log.e(TAG, "Event document does not exist.");
                    }

                    // Hide progress bar after loading
                    progressBar.setVisibility(ProgressBar.GONE);
                });
    }

    private void displayEventDetails(Event event) {
        // Set event details
        eventName.setText(event.getEventTitle());
        facilityName.setText(event.getOwnerFacility());
        address.setText(event.getEventAddress());  // Display event address

        // Load event image using Glide
        loadImage(event.getImageUrl());
    }

    private void loadImage(String imageUrl) {
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.event_place_holder)
                    .into(eventPosterimageView);
        } else {
            eventPosterimageView.setImageResource(R.drawable.event_place_holder);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            eventListener.remove(); // Remove the listener to avoid memory leaks
        }
    }


}