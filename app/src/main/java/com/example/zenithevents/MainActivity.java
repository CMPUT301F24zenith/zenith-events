package com.example.zenithevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Organizer.EventView;
import com.example.zenithevents.QRCodes.QRScannerActivity;
import com.example.zenithevents.User.OrganizerPage;
import com.example.zenithevents.User.UserPage;
import com.example.zenithevents.WaitingListPackage.WaitingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button buttonEntrant;
    Button organizerButton;
    Button buttonAdmin;
    Button buttonCreateEventEvent;
    Button scanQRButton;
    Button waitingListButton;
    Button createAProfile;
    Button applyEventButton;
    Button testEventView;

    private UserUtils userUtils;

    Button viewProfileButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userUtils = new UserUtils();
        mAuth = FirebaseAuth.getInstance();

        buttonEntrant = findViewById(R.id.entrantButton);
        organizerButton = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);
        scanQRButton = findViewById(R.id.scanQRButton);
        waitingListButton = findViewById(R.id.waitingListButton);
        createAProfile = findViewById(R.id.createAProfile);
        viewProfileButton = findViewById(R.id.viewProfileButton);
        applyEventButton = findViewById(R.id.applyEvent);
        testEventView = findViewById(R.id.testEventView);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            createAProfile.setEnabled(true);
        } else {
            viewProfileButton.setEnabled(true);
        }


        scanQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });
        waitingListButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WaitingList.class);
            startActivity(intent);
        });
        createAProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
            startActivity(intent);
        });
        viewProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserPage.class);
            startActivity(intent);
        });
        organizerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrganizerPage.class);
            startActivity(intent);
        });

        buttonEntrant.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EntrantViewActivity.class);
            startActivity(intent);
        });

        testEventView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventView.class);
            String eventId = "lUqyOyneNo4CgfNhS5OP";
            intent.putExtra("event_id", eventId);
            startActivity(intent);
        });

        applyEventButton.setOnClickListener(v -> {
            String eventId = "k";
            Context context = MainActivity.this;

            userUtils.applyEvent(context, eventId, isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(context, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to join event. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
