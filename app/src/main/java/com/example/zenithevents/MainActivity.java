package com.example.zenithevents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.LogInSignUP.LogInActivity;
import com.example.zenithevents.LogInSignUP.SignUpOption;
import com.example.zenithevents.WaitingListPackage.WaitingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    Button buttonEntrant;
    Button buttonOrganizer;
    Button buttonAdmin;
    Button buttonCreateEventEvent;
    Button buttonLogIn;
    Button buttonSignUp;
    Button waitingListButton;
    Button buttonLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();

        buttonEntrant = findViewById(R.id.entrantButton);
        buttonOrganizer = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);
        buttonCreateEventEvent = findViewById(R.id.createEventButton);
        buttonLogIn = findViewById(R.id.logInButton);
        buttonSignUp = findViewById(R.id.signUpButton);
        waitingListButton = findViewById(R.id.waitingListButton);
        buttonLogOut = findViewById(R.id.logOutButton);

        buttonLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        });
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpOption.class);
            startActivity(intent);
        });
        waitingListButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WaitingList.class);
            startActivity(intent);
        });

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            buttonLogOut.setVisibility(View.VISIBLE);

        } else {
            buttonLogOut.setVisibility(View.GONE);
        }
        buttonLogOut.setOnClickListener(view -> {
            auth.signOut();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            buttonLogOut.setVisibility(View.GONE);
        });

    }
}











