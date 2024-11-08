package com.example.zenithevents.Organizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.zenithevents.EntrantsList.CancelledEntrants;
import com.example.zenithevents.EntrantsList.EnrolledEntrants;
import com.example.zenithevents.EntrantsList.SampledEntrants;
import com.example.zenithevents.EntrantsList.WaitlistedEntrants;
import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EventView extends AppCompatActivity {

    private static final String TAG = "EventView";


    ImageView eventPosterImageView, eventQRImageView;
    private Button btnJoinLeaveWaitingList, qrCodeButton, waitlistButton, cancelledButton;
    private Button selectedButton, registeredButton;
    private TextView eventDescription, eventName, facilityName, eventAddress;
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

        String eventId = getIntent().getStringExtra("event_id");
        initializeViews();
        setupRealTimeEventListener(eventId);

    }

    private void initializeViews() {
        eventPosterImageView = findViewById(R.id.eventImage);
        btnJoinLeaveWaitingList = findViewById(R.id.btnJoinWaitingList);
        facilityName = findViewById(R.id.facilityName);
        eventAddress = findViewById(R.id.eventAddress);
        progressBar = findViewById(R.id.progressBar);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        selectedButton = findViewById(R.id.selectedListButton);
        waitlistButton = findViewById(R.id.waitlistButton);
        registeredButton = findViewById(R.id.registeredListButton);
        cancelledButton = findViewById(R.id.cancelledListButton);
        qrCodeButton = findViewById(R.id.qrCodeButton);
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
        Log.d("FunctionCall", "displayingDetails");

        // Set event details
        eventName.setText(event.getEventTitle());
        facilityName.setText(event.getOwnerFacility());
        eventAddress.setText(event.getEventAddress());
        eventDescription.setText(event.getEventDescription());
        UserUtils userUtils = new UserUtils();
        String deviceID = DeviceUtils.getDeviceID(this);

        Log.d("FunctionCall", "deviceID: " + deviceID);

        if (event.getWaitingList().contains(deviceID) ||
                event.getSelected().contains(deviceID) ||
                event.getCancelledList().contains(deviceID) ||
                event.getRegistrants().contains(deviceID)
        ) {
            btnJoinLeaveWaitingList.setBackgroundColor(Color.RED);
            btnJoinLeaveWaitingList.setText("Leave Waiting List");

            btnJoinLeaveWaitingList.setOnClickListener(v -> {
                Context context = EventView.this;

                userUtils.applyLeaveEvent(context, deviceID, event.getEventId(), isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(context, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to join event. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            btnJoinLeaveWaitingList.setBackgroundColor(Color.BLUE);
            btnJoinLeaveWaitingList.setText("Join Waiting List");

            btnJoinLeaveWaitingList.setOnClickListener(v -> {
                Context context = EventView.this;

                userUtils.applyLeaveEvent(context, deviceID, event.getEventId(), isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(context, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to join event. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            if (event.getNumParticipants() != 0 &&
                    event.getCancelledList().size() + event.getSelected().size() + event.getRegistrants().size() + event.getWaitingList().size() >= event.getNumParticipants()) {
                btnJoinLeaveWaitingList.setEnabled(false);
                btnJoinLeaveWaitingList.setText("Event is full");
                btnJoinLeaveWaitingList.setBackgroundColor(Color.GRAY);
            }
        }

        // Load event image using Glide
        loadImage(event.getImageUrl(), eventPosterImageView);
        Log.d("FunctionCall", deviceID);
        if (Objects.equals(event.getOwnerFacility(), deviceID)) {
            waitlistButton.setOnClickListener(v -> {
                Log.d("FunctionCall", deviceID);
                Intent intent = new Intent(EventView.this, WaitlistedEntrants.class);
                intent.putExtra("eventId", event.getEventId());
                startActivity(intent);
            });

            selectedButton.setOnClickListener(v -> {
                Intent intent = new Intent(EventView.this, SampledEntrants.class);
                intent.putExtra("eventId", event.getEventId());
                startActivity(intent);
            });

            registeredButton.setOnClickListener(v -> {
                Intent intent = new Intent(EventView.this, EnrolledEntrants.class);
                intent.putExtra("eventId", event.getEventId());
                startActivity(intent);
            });

            cancelledButton.setOnClickListener(v -> {
                Intent intent = new Intent(EventView.this, CancelledEntrants.class);
                intent.putExtra("eventId", event.getEventId());
                startActivity(intent);
            });
        }

        qrCodeButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventView.this, QRView.class);
            intent.putExtra("Event", event);
            startActivity(intent);
        });
    }

    private void loadImage(String imageUrl, ImageView placeholder) {
        BitmapUtils bitmapUtils = new BitmapUtils();
        if (imageUrl != null) {
            Bitmap imgBitMap = bitmapUtils.decodeBase64ToBitmap(imageUrl);
            Glide.with(this).load(imgBitMap).into(placeholder);
        } else {
            placeholder.setImageResource(R.drawable.event_place_holder);
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