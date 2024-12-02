package com.example.zenithevents.Admin;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.example.zenithevents.User.ProfileDetailActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * FacilityDetail is an activity that displays detailed information about a specific facility.
 * It allows administrators to view facility details, associated events, and delete the facility.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class FacilityDetail extends AppCompatActivity {
    private static final String TAG = "FacilityDetail";
    private TextView facilityName, facilityEmail, facilityPhoneNumber;
    private ListView facilityEventsListView;
    private Button deleteFacilityButton;
    private EventArrayAdapter eventArrayAdapter;
    private List<Event> eventList = new ArrayList<>();
    private ListenerRegistration eventsListener;
    private LottieAnimationView deleteFacilityAnimation;
    private String facilityId;
    EventUtils eventUtils;
    FacilityUtils facilityUtils;

    /**
     * Called when the activity is first created. Initializes UI components, fetches facility events, and sets up listeners.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state, if any.
     */
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
        facilityUtils = new FacilityUtils();

        facilityName = findViewById(R.id.facilityName);
        facilityEmail = findViewById(R.id.facilityEmail);
        facilityPhoneNumber = findViewById(R.id.facilityPhoneNumber);
        facilityEventsListView = findViewById(R.id.facilityEventsListView);
        deleteFacilityButton = findViewById(R.id.deleteFacility);
        deleteFacilityAnimation = findViewById(R.id.deleteFacilityAnimation);

        eventArrayAdapter = new EventArrayAdapter(this, eventList, "admin", null);
        facilityEventsListView.setAdapter(eventArrayAdapter);

        facilityEventsListView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_animation)
        );
        Intent intent = getIntent();
        facilityId = intent.getStringExtra("facilityId");

        eventsListeners(facilityId);

        eventUtils.fetchFacilityEvents(facilityId, events -> {
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

        deleteFacilityButton.setOnClickListener(v-> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Facility")
                    .setMessage("Are you sure you want to delete this facility")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        playAnimation();
                        deleteFacilityButton.setVisibility(View.GONE);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    /**
     * Sets up a listener to monitor changes in events associated with the facility.
     *
     * @param facilityId The ID of the facility whose events are being monitored.
     */
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

    /**
     * Handles cleanup when the activity is destroyed, including removing Firestore listeners.
     */
    protected void onDestroy() {
        super.onDestroy();
        if (eventsListener != null) {
            eventsListener.remove();
        }
    }

    /**
     * Plays an animation when the facility is deleted and triggers the deletion process.
     */
    void playAnimation(){
        deleteFacilityAnimation.setVisibility(View.VISIBLE);
        deleteFacilityAnimation.playAnimation();
        deleteFacilityAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                deleteFacilityAnimation.setVisibility(View.VISIBLE);
                deleteFacilityButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                deleteFacilityAnimation.setVisibility(View.GONE);
                facilityUtils.deleteFacility(FacilityDetail.this, facilityId);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                deleteFacilityAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}