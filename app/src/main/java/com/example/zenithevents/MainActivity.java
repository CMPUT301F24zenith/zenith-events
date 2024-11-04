package com.example.zenithevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.CreatProfile.CreateProfileActivity;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.QRCodes.QRScannerActivity;
import com.example.zenithevents.WaitingListPackage.WaitingList;

public class MainActivity extends AppCompatActivity {
    Button buttonEntrant;
    Button buttonOrganizer;
    Button buttonAdmin;
    Button buttonCreateEventEvent;
    Button scanQRButton;
    Button waitingListButton;
    Button createAProfile;
    Button joinEventButton;

    private UserUtils userUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userUtils = new UserUtils();

        buttonEntrant = findViewById(R.id.entrantButton);
        buttonOrganizer = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);
        buttonCreateEventEvent = findViewById(R.id.createEventButton);
        scanQRButton = findViewById(R.id.scanQRButton);
        waitingListButton = findViewById(R.id.waitingListButton);
        createAProfile = findViewById(R.id.createAProfile);
        joinEventButton = findViewById(R.id.joinEvent);



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

        joinEventButton.setOnClickListener(v -> {
            String eventId = "e";
            Context context = MainActivity.this;

            userUtils.addEvent(context, eventId, isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(context, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to join event. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
