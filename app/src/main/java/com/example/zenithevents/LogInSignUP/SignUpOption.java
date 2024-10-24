package com.example.zenithevents.LogInSignUP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.R;

public class SignUpOption extends AppCompatActivity {
    private Button entrantSignUpButton, organizerSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_option);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        entrantSignUpButton = findViewById(R.id.Entrant_SignUp_button);
        organizerSignUpButton = findViewById(R.id.Organizer_SignUp_button);

        entrantSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpOption.this, EntrantSignUpActivity.class);
            startActivity(intent);
        });
        organizerSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpOption.this, OrganizerSignUpActivity.class);
            startActivity(intent);
        });

    }
}