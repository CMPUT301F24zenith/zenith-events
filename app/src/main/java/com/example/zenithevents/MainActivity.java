package com.example.zenithevents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.User.OrganizerPage;
import com.example.zenithevents.User.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity serves as the entry point for the application, providing buttons for users to
 * navigate to different sections of the app based on their roles, such as an entrant, organizer,
 * or admin. The activity handles Firebase authentication to manage user sessions.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 *
 * @see AppCompatActivity
 * @see FirebaseAuth
 */
public class MainActivity extends AppCompatActivity {
    Button buttonEntrant;
    Button organizerButton;
    Button buttonAdmin;

    private FirebaseAuth mAuth;

    /**
     * Initializes the activity, sets up button click listeners for navigation
     * and initializes Firebase authentication.
     *
     * @param savedInstanceState the saved state of the application.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        buttonEntrant = findViewById(R.id.entrantButton);
        organizerButton = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);



        organizerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrganizerPage.class);
            startActivity(intent);
        });

        buttonEntrant.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EntrantViewActivity.class);
            startActivity(intent);
        });
    }
}
