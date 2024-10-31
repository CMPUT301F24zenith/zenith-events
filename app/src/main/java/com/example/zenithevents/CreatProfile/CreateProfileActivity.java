package com.example.zenithevents.CreatProfile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Activity for creating a new user profile.
 */
public class CreateProfileActivity extends AppCompatActivity {



    private FirebaseFirestore db;
    private Button backButton, confirmButton;
    private EditText etEntrantFirstName, etEntrantLastName, etEntrantPhoneNumber, etEntrantEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_profile);
        EdgeToEdge.enable(this);
        // Initialize Firebase Auth and Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        backButton = findViewById(R.id.back_button);
        confirmButton = findViewById(R.id.btnEntrantConfirm);
        etEntrantFirstName = findViewById(R.id.etEntrantFirstName);
        etEntrantLastName = findViewById(R.id.etEntrantLastName);
        etEntrantPhoneNumber = findViewById(R.id.etEntrantPhoneNumber);
        etEntrantEmail = findViewById(R.id.etEntrantEmail);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
        confirmButton.setOnClickListener(v -> {
            createProfile();
        });





    }

    /**
     * Creates a new user profile in the database.
     * This is a helper function that is later called in the onCreate() method.
     */

    private void createProfile() {
        String firstName = etEntrantFirstName.getText().toString();
        String lastName = etEntrantLastName.getText().toString();
        String phoneNumber = etEntrantPhoneNumber.getText().toString();
        String email = etEntrantEmail.getText().toString();

        String deviceID = DeviceUtils.getDeviceID(this);

        User user = new User(deviceID, firstName, lastName, email, phoneNumber);
        Map<String, Object> userData = UserUtils.convertUserToMap(user);
        db.collection("users")
                .document(deviceID)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    // Profile created successfully
                    Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error creating profile
                    Toast.makeText(this, "Error creating profile", Toast.LENGTH_SHORT).show();
                });
    }

}