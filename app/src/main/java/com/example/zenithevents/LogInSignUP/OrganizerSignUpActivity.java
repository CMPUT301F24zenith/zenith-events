package com.example.zenithevents.LogInSignUP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.MainActivity;
import com.example.zenithevents.R;

public class OrganizerSignUpActivity extends AppCompatActivity {
    private EditText organizerNameEditText, phoneNumberEditText, emailEditText, passwordEditText;
    private Button signUpButton, backButton;
    private TextView alreadyHaveAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.back_button);
        organizerNameEditText = findViewById(R.id.etOrganizerOrganizerName);
        phoneNumberEditText = findViewById(R.id.etOrganizerPhoneNumber);
        emailEditText = findViewById(R.id.etOrganizerEmail);
        passwordEditText = findViewById(R.id.etOrganizerPassword);
        signUpButton = findViewById(R.id.btnSignUp);
        alreadyHaveAccountTextView = findViewById(R.id.tvAlreadyHaveAccount);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerSignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });
        alreadyHaveAccountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerSignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });




    }
}