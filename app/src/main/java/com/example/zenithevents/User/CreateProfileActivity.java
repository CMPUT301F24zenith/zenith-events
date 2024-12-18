package com.example.zenithevents.User;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.HelperClasses.ValidationUtils;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

/**
 * Activity for creating a new user profile with anonymous authentication.
 *
 * <p>This activity allows users to create a profile by entering their first name, last name, email,
 * and phone number. If the input data is valid, it initiates an anonymous sign-in process with Firebase,
 * then saves the user's profile information to Firestore.
 *
 * <p><strong>AI-Generated Documentation:</strong> The Javadocs for this class were generated
 * with assistance from a generative AI language model. This model provided initial documentation
 * drafts based on the code structure, including method descriptions, parameter details, and explanations
 * of functionality. These drafts were then reviewed and refined to ensure clarity and accuracy, with adjustments
 * made for context and readability.
 *
 * <p>This AI-generated documentation aims to enhance code readability, reduce development time, and provide
 * a clear understanding of each method’s purpose and usage within the class.
 */
public class CreateProfileActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button  confirmButton;
    private CheckBox notifsCheckBox;
    private boolean wantsNotifs;
    private EditText etEntrantFirstName, etEntrantLastName, etEntrantPhoneNumber, etEntrantEmail;
    private LottieAnimationView animationView;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        type = getIntent().getStringExtra("type");

        EdgeToEdge.enable(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        etEntrantFirstName = findViewById(R.id.etEntrantFirstName);
        etEntrantLastName = findViewById(R.id.etEntrantLastName);
        etEntrantPhoneNumber = findViewById(R.id.etEntrantPhoneNumber);
        etEntrantEmail = findViewById(R.id.etEntrantEmail);
        confirmButton = findViewById(R.id.btnEntrantConfirm);
        notifsCheckBox = findViewById(R.id.notifsCheckBox);
        animationView = findViewById(R.id.confirm);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notifsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                wantsNotifs = true;
                checkAndRequestNotificationPermission();

            } else {
                wantsNotifs = false;
            }
        });


        confirmButton.setOnClickListener(v -> validateAndSignIn());
    }

    /**
     * Validates input fields and initiates anonymous sign-in if inputs are valid.
     *
     * <p>This method checks if the user's first name, last name, and email are provided, and that the email format
     * is valid. If the inputs are correct, it calls {@link #signInAnonymously(String, String, String, String)}
     * to start the sign-in process.
     *
     * <p><strong>AI-Generated Documentation:</strong> This Javadoc was generated with assistance from a generative AI
     * language model, then refined for clarity and accuracy.
     */
    private void validateAndSignIn() {
        String firstName = etEntrantFirstName.getText().toString().trim();
        String lastName = etEntrantLastName.getText().toString().trim();
        String phoneNumber = etEntrantPhoneNumber.getText().toString().trim();
        String email = etEntrantEmail.getText().toString().trim();

        boolean isValid = true;
        if (firstName.isEmpty()) {
            etEntrantFirstName.setError("First name is required");
            etEntrantFirstName.requestFocus();
            isValid = false;
        }
        if (lastName.isEmpty()) {
            etEntrantLastName.setError("Last name is required");
            if (isValid) {
                etEntrantLastName.requestFocus();
            }
            isValid = false;
        }
        if (email.isEmpty()) {
            etEntrantEmail.setError("Email is required");
            if (isValid) {
                etEntrantEmail.requestFocus();
            }
            isValid = false;
        }
        if (!email.isEmpty() && !ValidationUtils.isValidEmail(email)) {
            etEntrantEmail.setError("Invalid email format");
            if (isValid) {
                etEntrantEmail.requestFocus();
            }
            isValid = false;
        }
        if (isValid) {
            signInAnonymously(firstName, lastName, email, phoneNumber);
        }
    }



    /**
     * Signs in the user anonymously using Firebase Authentication.
     *
     * <p>This method initiates an anonymous sign-in with Firebase. If successful, it retrieves the user's
     *
     * profile in Firestore. If sign-in fails, an error message is displayed.
     *
     * <p><strong>AI-Generated Documentation:</strong> This Javadoc was generated with assistance from a generative AI
     * language model, then refined for clarity and accuracy.
     *
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email address.
     * @param phoneNumber The user's phone number.
     */
    private void signInAnonymously(String firstName, String lastName, String email, String phoneNumber) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {

                            String deviceId = new DeviceUtils().getDeviceID(this);;
                            createProfile(currentUser.getUid(),deviceId, firstName, lastName, email, phoneNumber);
                        }
                    } else {
                        Toast.makeText(CreateProfileActivity.this, "Anonymous authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Creates a new user profile in the database.
     *
     * <p>This method stores the user profile data in Firestore under a document identified by the user's unique ID.
     * On success, the user is redirected to {@link MainActivity}, and the current activity is finished. On failure,
     * an error message is displayed.
     *
     * <p><strong>AI-Generated Documentation:</strong> This Javadoc was generated with assistance from a generative AI
     * language model, then refined for clarity and accuracy.
     * @param AnonymousAuthID The user's unique ID.
     * @param deviceId The ID of the authenticated user.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email address.
     * @param phoneNumber The user's phone number.
     */
    private void createProfile(String AnonymousAuthID,String deviceId, String firstName, String lastName, String email, String phoneNumber) {
        // TODO change the function to get the full user object
        User userProfile = new User(deviceId, firstName, lastName, email, phoneNumber);
        userProfile.setAnonymousAuthID(AnonymousAuthID);
        userProfile.setWantsNotifs(wantsNotifs);
        Map<String, Object> userData = UserUtils.convertUserToMap(userProfile);
        db.collection("users")
                .document(deviceId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                    playAnimation();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error creating profile", Toast.LENGTH_SHORT).show());
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                notifsCheckBox.setChecked(true);
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                notifsCheckBox.setChecked(false);
            }
        }
    }


    void playAnimation(){

        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                animationView.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }


}