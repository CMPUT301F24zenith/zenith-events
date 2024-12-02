package com.example.zenithevents.EntrantsList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.example.zenithevents.User.ProfileDetailActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * The MapActivity class handles the display of user locations on a map for a specific event.
 * It uses Google Maps to show markers for user locations stored in Firestore and allows interaction
 * with the markers to view user profiles.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String eventID;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    /**
     * Initializes the activity and sets up the map fragment and Firestore reference.
     * Retrieves the event ID from the intent to query user locations.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("MapActivity", "Map fragment is null");
        }

        eventID = getIntent().getStringExtra("EventID");
    }

    /**
     * Called when the map is ready for use. Fetches user locations from Firestore
     * and displays them as markers on the map.
     *
     * @param googleMap The GoogleMap object to interact with.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        DocumentReference eventRef = db.collection("events").document(eventID);
        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            progressBar.setVisibility(View.GONE);
            if (documentSnapshot.exists()) {
                Map<String, Object> userLocationsMap = (Map<String, Object>) documentSnapshot.get("userLocations");
                if (userLocationsMap != null) {
                    // Convert userLocations to a HashMap
                    HashMap<String, Map<String, Object>> userLocations = new HashMap<>();
                    for (String userId : userLocationsMap.keySet()) {
                        Map<String, Object> coordinates = (Map<String, Object>) userLocationsMap.get(userId);
                        userLocations.put(userId, coordinates);
                    }
                    displayMarkersOnMap(userLocations);
                } else {
                    Log.d("Firestore", "No userLocations found for event: " + eventID);
                }
            } else {
                Log.e("Firestore", "Event document does not exist");
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Log.e("FirestoreError", "Failed to retrieve event", e);
        });

        mMap.setOnMarkerClickListener(marker -> {
            String userID = (String) marker.getTag();
            Intent intent = new Intent(MapActivity.this, ProfileDetailActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("type", "organizer");
            startActivity(intent);
            return true;
        });
    }

    /**
     * Displays user locations on the map as markers and adjusts the camera to fit all markers.
     *
     * @param userLocations A map of user IDs to their latitude and longitude coordinates.
     */
    private void displayMarkersOnMap(HashMap<String, Map<String, Object>> userLocations) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (String userId : userLocations.keySet()) {
            Map<String, Object> coordinates = userLocations.get(userId);
            if (coordinates != null) {
                Double latitude = (Double) coordinates.get("latitude");
                Double longitude = (Double) coordinates.get("longitude");

                if (latitude != null && longitude != null) {
                    LatLng userLatLng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(userLatLng);
                    mMap.addMarker(markerOptions).setTag(userId);
                    boundsBuilder.include(userLatLng);
                }
            }
        }

        LatLngBounds bounds = boundsBuilder.build();
        int padding = 100;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cameraUpdate);
    }
}
