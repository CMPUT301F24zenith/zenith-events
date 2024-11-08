package com.example.zenithevents.Facility;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.example.zenithevents.Manifest;
import android.Manifest;

import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.ValidationUtils;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;
import com.example.zenithevents.User.OrganizerPage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreateFacility extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private Button saveButton;
    private EditText facilityName;
    private EditText facilityPhone;
    private EditText facilityEmail;
    private Uri uploadedImageUrl;
    private FirebaseFirestore db;
    private String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_facility);

        db = FirebaseFirestore.getInstance();
        deviceId = DeviceUtils.getDeviceID(this);

        facilityName = findViewById(R.id.location_name);
        facilityPhone = findViewById(R.id.location_phone);
        facilityEmail = findViewById(R.id.location_email);
        saveButton = findViewById(R.id.location_save);

        loadFacility(deviceId);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = facilityName.getText().toString().trim();
                String phone = facilityPhone.getText().toString().trim();
                String email = facilityEmail.getText().toString().trim();

                if (checkFields(name, email)) {
                    Facility facility = new Facility(name, phone, email, deviceId);
                    storeFacility(facility);
                }

            }
        });
    }

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

    private void storeFacility(Facility facility) {
        db.collection("facilities").document(deviceId)  // Set document ID to deviceId
                .set(facility)
                .addOnSuccessListener(aVoid -> changeToView())
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving facility", Toast.LENGTH_SHORT).show());
    }

    private void changeToView() {
        Intent viewIntent = new Intent(CreateFacility.this, OrganizerPage.class);
        viewIntent.putExtra("deviceId", deviceId);
        startActivity(viewIntent);
        finish();
        Toast.makeText(CreateFacility.this, "Facility saved", Toast.LENGTH_SHORT).show();
    }
}