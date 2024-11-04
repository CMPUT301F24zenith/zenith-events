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

import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Activity for creating a new user profile with anonymous authentication.
 */
public class CreateProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button backButton, confirmButton;
    private EditText etEntrantFirstName, etEntrantLastName, etEntrantPhoneNumber, etEntrantEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);


        EdgeToEdge.enable(this);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInAnonymously();
        } else {

            Toast.makeText(this, "Anonymous user authenticated", Toast.LENGTH_SHORT).show();
        }


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
            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Signs in the user anonymously.
     */
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateProfileActivity.this, "Anonymous authentication successful", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(CreateProfileActivity.this, "Anonymous authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Creates a new user profile in the database.
     */
    private void createProfile() {
        String firstName = etEntrantFirstName.getText().toString();
        String lastName = etEntrantLastName.getText().toString();
        String phoneNumber = etEntrantPhoneNumber.getText().toString();
        String email = etEntrantEmail.getText().toString();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = user.getUid();


        User userProfile = new User(userId, firstName, lastName, email, phoneNumber);
        Map<String, Object> userData = UserUtils.convertUserToMap(userProfile);


        db.collection("users")
                .document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error creating profile", Toast.LENGTH_SHORT).show());
    }
}
