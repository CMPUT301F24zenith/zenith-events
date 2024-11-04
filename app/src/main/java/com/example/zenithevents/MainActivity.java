package com.example.zenithevents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.CreatProfile.CreateProfileActivity;
import com.example.zenithevents.User.UserPage;
import com.example.zenithevents.WaitingListPackage.WaitingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button buttonEntrant;
    Button buttonOrganizer;
    Button buttonAdmin;
    Button buttonCreateEventEvent;
    Button waitingListButton;
    Button createAProfile;
    Button viewProfileButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        buttonEntrant = findViewById(R.id.entrantButton);
        buttonOrganizer = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);
        buttonCreateEventEvent = findViewById(R.id.createEventButton);
        waitingListButton = findViewById(R.id.waitingListButton);
        createAProfile = findViewById(R.id.createAProfile);
        viewProfileButton = findViewById(R.id.viewProfileButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            createAProfile.setEnabled(false);
        }



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




    }
}











