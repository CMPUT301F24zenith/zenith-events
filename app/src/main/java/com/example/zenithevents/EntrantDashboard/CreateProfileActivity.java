package com.example.zenithevents.EntrantDashboard;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zenithevents.EntrantsList.SampledEntrants;
import com.example.zenithevents.Events.EventView;
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

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button  confirmButton;
    private CheckBox notifsCheckBox;
    private boolean wantsNotifs;
    private EditText etEntrantFirstName, etEntrantLastName, etEntrantPhoneNumber, etEntrantEmail;
    private LottieAnimationView confirmAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        EdgeToEdge.enable(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        etEntrantFirstName = findViewById(R.id.etEntrantFirstName);
        etEntrantLastName = findViewById(R.id.etEntrantLastName);
        etEntrantPhoneNumber = findViewById(R.id.etEntrantPhoneNumber);
        etEntrantEmail = findViewById(R.id.etEntrantEmail);
        confirmButton = findViewById(R.id.btnEntrantConfirm);
        notifsCheckBox = findViewById(R.id.notifsCheckBox);
        confirmAnimation = findViewById(R.id.confirm);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notifsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(CreateProfileActivity.this,
                                Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CreateProfileActivity.this,
                                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                    101);
                        }
                    }
                    wantsNotifs = true;
//                    Toast.makeText(CreateProfileActivity.this, "Notifications enabled", Toast.LENGTH_SHORT).show();
                } else {
                    wantsNotifs = false;
                    Toast.makeText(CreateProfileActivity.this, "Notifications disabled", Toast.LENGTH_SHORT).show();
                }

            }
        });


        confirmButton.setOnClickListener(v -> {
            validateAndSignIn();
        });
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


        if (firstName.isEmpty()) {
            etEntrantFirstName.setError("First name is required");
            return;
        }

        if (lastName.isEmpty()) {
            etEntrantFirstName.setError("Last name is required");
            return;
        }
        if (email.isEmpty()) {
            etEntrantFirstName.setError("Email is required");
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            etEntrantEmail.setError("Invalid email format");
            return;
        }

        signInAnonymously(firstName, lastName, email, phoneNumber);
    }

    /**
     * Signs in the user anonymously using Firebase Authentication.
     *
     * <p>This method initiates an anonymous sign-in with Firebase. If successful, it retrieves the user's
     * unique ID and calls {@link #createProfile(String, String, String, String, String)} to store the user’s
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
                        Log.d("ProfileCreation", "Anonymous authentication successful");
                        if (currentUser != null) {

                            String deviceId = new DeviceUtils().getDeviceID(CreateProfileActivity.this);;
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
                    confirmAnimation.setVisibility(View.VISIBLE);
                    confirmAnimation.playAnimation();
                    confirmAnimation.addAnimatorListener(new Animator.AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                            // Animation started
                            confirmAnimation.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            confirmAnimation.setVisibility(View.GONE);
                            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                            startActivity(intent);



                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                            confirmAnimation.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    Log.d("ProfileCreation", "Profile created successfully");

                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error creating profile", Toast.LENGTH_SHORT).show());
    }

}
