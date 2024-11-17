package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.InitialsGenerator;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProfileDetailActivity";
    private TextView FirstNameTextView, LastNameTextView, EmailTextView, PhoneNumberTextView, initialsTextView;
    private UserUtils userUtils;
    private Button btnRomove;
    private ProgressBar progressBar;
    private String profileImage;
    private FirebaseFirestore db;
    private ImageView profileImageView;
    private String initials;
    private String userID;
    private Button deleteUserButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        FirstNameTextView = findViewById(R.id.viewFirstName);
        LastNameTextView = findViewById(R.id.viewLastName);
        EmailTextView = findViewById(R.id.viewEmail);
        PhoneNumberTextView = findViewById(R.id.viewPhoneNumber);
        btnRomove = findViewById(R.id.btnRemove);
        initialsTextView = findViewById(R.id.initials);
        progressBar = findViewById(R.id.progressBar);
        deleteUserButton = findViewById(R.id.deleteProfile);
        userUtils = new UserUtils();
        db = FirebaseFirestore.getInstance();
        profileImageView = findViewById(R.id.profileImage);
        fetchUserProfile();
    }

    /**
     * Fetches the user's profile from the database and populates the UI fields with the retrieved data.
     */
    private void fetchUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        userUtils.fetchUserProfile(userID, user -> {
            if (user != null) {
                FirstNameTextView.setText(user.getFirstName() != null ? user.getFirstName() : "");
                LastNameTextView.setText(user.getLastName() != null ? user.getLastName() : "");
                EmailTextView.setText(user.getEmail() != null ? user.getEmail() : "");
                PhoneNumberTextView.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");

                profileImage = user.getProfileImageURL();
                String firstName = FirstNameTextView.getText().toString();
                String lastName = LastNameTextView.getText().toString();
                initials = InitialsGenerator.getInitials(firstName, lastName);


                if (profileImage != null && !profileImage.isEmpty()) {

                    Bitmap decodedProfileImage = BitmapUtils.decodeBase64ToBitmap(profileImage);
                    profileImageView.setImageBitmap(decodedProfileImage);
                    initialsTextView.setVisibility(View.GONE);
                } else {

                    initialsTextView.setText(initials.toUpperCase());
                    profileImageView.setImageResource(R.drawable.circle_background);
                    initialsTextView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

}