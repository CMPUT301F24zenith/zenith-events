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
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntrantSignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText, emailEditText, passwordEditText;
    private Button signUpButton, backButton;;
    private TextView alreadyHaveAccountTextView;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entrant_sign_up);
        EdgeToEdge.enable(this);
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firstNameEditText = findViewById(R.id.etEntrantFirstName);
        emailEditText = findViewById(R.id.etEntrantEmail);
        passwordEditText = findViewById(R.id.etEntrantPassword);
        signUpButton = findViewById(R.id.btnEntrantSignUp);
        lastNameEditText = findViewById(R.id.etEntrantLastName);

        phoneNumberEditText = findViewById(R.id.etEntrantPhoneNumber);



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
        signUpButton.setOnClickListener(v -> createEntrantUser());



    }

    private void createEntrantUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if(firstName.isEmpty()){
            firstNameEditText.setError("First Name is Required");
            firstNameEditText.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            lastNameEditText.setError("Last Name is Required");
            lastNameEditText.requestFocus();
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

                            Entrant entrant = new Entrant("Entrant",generatedID, firstName, lastName, email, finalPhoneNumber);
                            db.collection("Users")
                                    .document(generatedID)
                                    .set(entrant)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EntrantSignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EntrantSignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EntrantSignUpActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                    else {
                        Toast.makeText(EntrantSignUpActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                    }

                });

    }


}