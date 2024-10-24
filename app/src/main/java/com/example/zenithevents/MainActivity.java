package com.example.zenithevents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.LogInSignUP.EntrantSignUpActivity;
import com.example.zenithevents.LogInSignUP.LogInActivity;
import com.example.zenithevents.LogInSignUP.SignUpOption;

public class MainActivity extends AppCompatActivity {

    Button buttonEntrant;
    Button buttonOrganizer;
    Button buttonAdmin;
    Button buttonCreateEventEvent;
    Button buttonLogIn;
    Button buttonSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonEntrant = findViewById(R.id.entrantButton);
        buttonOrganizer = findViewById(R.id.organizerButton);
        buttonAdmin = findViewById(R.id.adminButton);
        buttonCreateEventEvent = findViewById(R.id.createEventButton);
        buttonLogIn = findViewById(R.id.logInButton);
        buttonSignUp = findViewById(R.id.signUpButton);

        buttonLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        });
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpOption.class);
            startActivity(intent);
        });
    }


}



