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

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, backButton;
    private TextView signUpTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.Email_editText);
        passwordEditText = findViewById(R.id.Password_editText);
        loginButton = findViewById(R.id.LogIn_button);
        signUpTextView = findViewById(R.id.SignUp_textView);
        backButton = findViewById(R.id.back_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
        });

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, SignUpOption.class);
            startActivity(intent);
        });

    }
}