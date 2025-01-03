package com.example.zenithevents.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zenithevents.EntrantsList.EventsFragment;
import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.HelperClasses.DeviceUtils;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.FirestoreEventsCollection;
import com.example.zenithevents.HelperClasses.FirestoreFacilitiesCollection;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * OrganizerPage is an Android activity that provides functionality for an event organizer
 * to manage events and facilities. This page allows organizers to create events, create facilities,
 * and view facilities, with visibility settings based on the existence of facility data in Firestore.
 * It uses Firebase Firestore for backend data retrieval and Edge-to-Edge window insets for layout adjustments.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 *
 * @see AppCompatActivity
 * @see FirebaseFirestore
 * @see DeviceUtils
 * @see EventsFragment
 */
public class OrganizerPage extends AppCompatActivity {
    ImageButton createEventButton, viewFacilityButton;
    Button createFacilityButton;
    String deviceId;

    private FirebaseFirestore db;
    private ProgressBar progressBar;
    FrameLayout myEventsFragment;

    /**
     * Initializes the activity, sets up Firebase Firestore, and handles button visibility based on
     * facility existence in Firestore.
     *
     * @param savedInstanceState the saved state of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        deviceId = DeviceUtils.getDeviceID(this);

        FirestoreFacilitiesCollection.listenForSpecificFacilityChanges(deviceId, facility -> {
            checkFacilityExists();
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        myEventsFragment = findViewById(R.id.myEventsFragment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myEventsFragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        createEventButton = findViewById(R.id.createEventButton);
        progressBar = findViewById(R.id.progressBar);
        createFacilityButton = findViewById(R.id.createFacilityButton);
        viewFacilityButton = findViewById(R.id.viewFacilityButton);
        createEventButton = findViewById(R.id.createEventButton);

        progressBar.setVisibility(View.VISIBLE);
        createFacilityButton.setVisibility(View.GONE);
        viewFacilityButton.setVisibility(View.GONE);
        createEventButton.setVisibility(View.GONE);
        myEventsFragment.setVisibility(View.GONE);


        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateEventPage.class);
            intent.putExtra("Event Id", "");
            Log.d("FunctionCall", "11,1");
            startActivity(intent);
        });

        Bundle args = new Bundle();
        args.putString("type", "organizer");
        loadFragment(new EventsFragment(), args);

        createFacilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateFacility.class);
            intent.putExtra("deviceId", deviceId);
            startActivity(intent);
        });

        viewFacilityButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewFacility.class);
            intent.putExtra("deviceId", deviceId);
            startActivity(intent);
        });
    }

    /**
     * Checks if a facility associated with the device exists in Firestore and updates
     * button visibility accordingly.
     */
    private void checkFacilityExists() {
        db.collection("facilities").document(deviceId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);

                    if (documentSnapshot.exists()) {
                        createFacilityButton.setVisibility(View.GONE);
                        viewFacilityButton.setVisibility(View.VISIBLE);
                        createEventButton.setVisibility(View.VISIBLE);
                        myEventsFragment.setVisibility(View.VISIBLE);
                    } else {
                        createFacilityButton.setVisibility(View.VISIBLE);
                        myEventsFragment.setVisibility(View.GONE);
                        viewFacilityButton.setVisibility(View.GONE);
                        createEventButton.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error checking facility", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Loads a fragment into the specified container with provided arguments.
     *
     * @param fragment the fragment to load.
     * @param args     the arguments to pass to the fragment.
     */
    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myEventsFragment, fragment);
        fragmentTransaction.commit();
    }
}
