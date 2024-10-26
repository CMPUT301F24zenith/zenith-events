package com.example.zenithevents.LogInSignUP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.MainActivity;
import com.example.zenithevents.Objects.Entrant;
import com.example.zenithevents.Objects.Organizer;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizerSignUpActivity extends AppCompatActivity {
    private EditText organizerNameEditText, phoneNumberEditText, emailEditText, passwordEditText;
    private Button signUpButton, backButton;
    private TextView alreadyHaveAccountTextView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

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
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
        signUpButton.setOnClickListener(v -> createEntrantUser());




    }
    private void createEntrantUser() {

        String name = organizerNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if(name.isEmpty()){
            organizerNameEditText.setError("Organizer Name is Required");
            organizerNameEditText.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailEditText.setError("Email is Required");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Password is Required");
            passwordEditText.requestFocus();
            return;
        }
        if(phoneNumber.isEmpty()){
            phoneNumber = "NA";
        }

        String finalPhoneNumber = phoneNumber;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if(firebaseUser != null){
                            String generatedID = firebaseUser.getUid();

                            Organizer organizer = new Organizer("Organizer",generatedID, name, email, finalPhoneNumber);
                            db.collection("Users")
                                    .document(generatedID)
                                    .set(organizer)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(OrganizerSignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OrganizerSignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(OrganizerSignUpActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                    else {
                        Toast.makeText(OrganizerSignUpActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                    }

                });

    }
}