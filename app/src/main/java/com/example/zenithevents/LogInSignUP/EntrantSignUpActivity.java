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

public class EntrantSignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText, organizerNameEditText, emailEditText, passwordEditText;
    private Button signUpButton, backButton;;
    private TextView alreadyHaveAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entrant_sign_up);
        EdgeToEdge.enable(this);

        backButton = findViewById(R.id.back_button);
        alreadyHaveAccountTextView = findViewById(R.id.tvAlreadyHaveAccount);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantSignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });
        alreadyHaveAccountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantSignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });


    }
}