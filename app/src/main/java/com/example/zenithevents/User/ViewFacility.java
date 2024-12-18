package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Activity to view a facility's details.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class ViewFacility extends AppCompatActivity {

    private TextView facilityName;
    private TextView facilityPhone;
    private TextView facilityEmail;
    private Button editButton;
    private FirebaseFirestore db;
    private String deviceId;

    /**
     * Called when the activity is created. Initializes the UI elements and loads the facility data.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_facility);

        db = FirebaseFirestore.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");

        facilityName = findViewById(R.id.namefield);
        facilityPhone = findViewById(R.id.phonefield);
        facilityEmail = findViewById(R.id.emailfield);
        editButton = findViewById(R.id.editbuttonfield);

        FacilityUtils.listenForFacilitiesChanges(facilities -> {
            if (deviceId != null) {
                showFacilityData(deviceId);
            } else {
                Toast.makeText(this, "Error in getting Facility", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        editButton.setOnClickListener(v-> {
            Intent editIntent = new Intent(this, CreateFacility.class);
            editIntent.putExtra("deviceId", deviceId);
            startActivity(editIntent);
        });

    }

    /**
     * Loads and displays the facility's data from Firestore using the device ID.
     *
     * @param deviceId The device ID to fetch the facility details.
     */
    private void showFacilityData(String deviceId) {
        DocumentReference facilityRef = db.collection("facilities").document(deviceId);
        facilityRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Facility facility = documentSnapshot.toObject(Facility.class);
                if (facility != null) {
                    facilityName.setText(facility.getNameOfFacility());
                    facilityPhone.setText(facility.getPhoneOfFacility());
                    facilityEmail.setText(facility.getEmailOfFacility());
                }
            } else {
                Toast.makeText(this, "Facility not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Cannot load Facility", Toast.LENGTH_SHORT).show());
    }
}
