package com.example.zenithevents.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.zenithevents.Manifest;

import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.ValidationUtils;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * Activity that allows users to create and edit a facility profile, saving data to Firestore.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class CreateFacility extends AppCompatActivity {

    private Button saveButton;
    private EditText facilityName;
    private EditText facilityPhone;
    private EditText facilityEmail;
    private FirebaseFirestore db;
    private String deviceId;
    String type;

    /**
     * Called when the activity is created. Initializes the UI elements and loads existing facility data if available.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_facility);

        db = FirebaseFirestore.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        type = getIntent().getStringExtra("type");

        facilityName = findViewById(R.id.facility_name);
        facilityPhone = findViewById(R.id.facility_phone);
        facilityEmail = findViewById(R.id.facility_email);
        saveButton = findViewById(R.id.facility_save);

        loadFacility(deviceId);


        saveButton.setOnClickListener(view -> {
            String name = facilityName.getText().toString().trim();
            String phone = facilityPhone.getText().toString().trim();
            String email = facilityEmail.getText().toString().trim();

            if (checkFields(name, email)) {
                Facility facility = new Facility(name, phone, email, deviceId);
                storeFacility(facility);
            }
        });
    }

    /**
     * Validates the input fields for required data.
     *
     * @param name The facility name.
     * @param email The facility email.
     * @return True if all required fields are valid, otherwise false.
     */
    private boolean checkFields(String name, String email) {
        if (name.isEmpty()) {
            facilityName.setError("Field is required.");
            facilityName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            facilityEmail.setError("Email is required");
            facilityEmail.requestFocus();
            return false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            facilityEmail.setError("Invalid email format");
            return false;
        }
        return true;
    }

    /**
     * Loads an existing facility's data from Firestore based on the device ID.
     *
     * @param deviceId The device ID to identify the facility.
     */
    private void loadFacility(String deviceId) {
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
                Toast.makeText(this, "No facility found for this device", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Cannot load facility", Toast.LENGTH_SHORT).show());
    }

    /**
     * Stores the facility data in Firestore.
     *
     * @param facility The facility object containing the data to be stored.
     */
    private void storeFacility(Facility facility) {
        db.collection("facilities").document(deviceId)  // Set document ID to deviceId
                .set(facility)
                .addOnSuccessListener(aVoid -> changeToView())
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving facility", Toast.LENGTH_SHORT).show());
    }

    /**
     * Navigates to the OrganizerPage and displays a success message.
     */
    private void changeToView() {
        if (Objects.equals(type, "Edit Facility")) {
            finish();
        } else if (Objects.equals(type, "Create Facility")) {
            Intent viewIntent = new Intent(this, OrganizerPage.class);
            viewIntent.putExtra("deviceId", deviceId);
            startActivity(viewIntent);
            finish();
        }
        Toast.makeText(this, "Facility saved", Toast.LENGTH_SHORT).show();
    }
}