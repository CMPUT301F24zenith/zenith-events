package com.example.zenithevents.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.test.espresso.remote.EspressoRemoteMessage;

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FacilityDetail extends AppCompatActivity {
    private static final String TAG = "FacilityDetail";
    private TextView facilityName, facilityEmail, facilityPhoneNumber;
    private ListView facilityEventsListView;
    private Button deleteFacilityButton;
    private EventArrayAdapter eventArrayAdapter;
    private List<Event> eventList = new ArrayList<>();
    EventUtils eventUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facility_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left + v.getPaddingLeft(),
                    systemBars.top + v.getPaddingTop(),
                    systemBars.right + v.getPaddingRight(),
                    systemBars.bottom + v.getPaddingBottom()
            );
            return insets;
        });
        eventUtils = new EventUtils();



        facilityName = findViewById(R.id.facilityName);
        facilityEmail = findViewById(R.id.facilityEmail);
        facilityPhoneNumber = findViewById(R.id.facilityPhoneNumber);
        facilityEventsListView = findViewById(R.id.facilityEventsListView);
        deleteFacilityButton = findViewById(R.id.deleteFacility);

        eventArrayAdapter = new EventArrayAdapter(this, eventList, "organizer", null);
        facilityEventsListView.setAdapter(eventArrayAdapter);

        facilityEventsListView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_animation)
        );
        Intent intent = getIntent();
        String facilityID = intent.getStringExtra("facilityID");

        eventsListeners(facilityID);

        String deviceId = DeviceUtils.getDeviceID(this);
        eventUtils.fetchOrganizerEvents(this, facilityID, events -> {
            if (events != null) {
                eventList = events;
                eventArrayAdapter.clear();
                eventArrayAdapter.addAll(eventList);
                eventArrayAdapter.notifyDataSetChanged();
                facilityEventsListView.scheduleLayoutAnimation();
            }
        });

        facilityName.setText(intent.getStringExtra("facilityName"));
        facilityEmail.setText(intent.getStringExtra("facilityEmail"));
        facilityPhoneNumber.setText(intent.getStringExtra("facilityPhoneNumber"));

        deleteFacilityButton.setOnClickListener(v-> deleteFacilityConfirmation(deviceId));
    }

    private void eventsListeners(String facilityId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventsListener = db.collection("events")
                .whereEqualTo("ownerFacility", facilityId)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Cannot get events", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        eventList.clear();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Event event = document.toObject(Event.class);
                            if (event != null) {
                                eventList.add(event);
                            }
                        }
                        eventArrayAdapter.clear();
                        eventArrayAdapter.addAll(eventList);
                        eventArrayAdapter.notifyDataSetChanged();
                        facilityEventsListView.scheduleLayoutAnimation();
                    }
                });
    }

    private void deleteFacilityConfirmation(String deviceId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Facility")
                .setMessage("Are you sure you want to delete this facility")
                .setPositiveButton("Delete", (dialog, which) -> deleteFacilityAndEvents(deviceId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteFacilityAndEvents(String deviceId) {
        Toast.makeText(this, "Deleting facility and its events", Toast.LENGTH_SHORT).show();
        eventUtils.fetchOrganizerEvents(this, deviceId, events -> {
            if (events != null) {
                for (Event event : events) {
                    eventUtils.deleteEvent(event.getEventId(), isDeleted -> {
                        if (!isDeleted) {
                            Toast.makeText(FacilityDetail.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            deleteFacility(deviceId);
        });

    }

    private void deleteFacility(String deviceId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facilities").document(deviceId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FacilityDetail.this, "Facility deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e-> {
                    Toast.makeText(FacilityDetail.this, "Facility failed to delete", Toast.LENGTH_SHORT).show();
                });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (eventsListener != null) {
            eventsListener.remove();
        }
    }


}