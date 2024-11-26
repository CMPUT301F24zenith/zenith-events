package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.InitialsGenerator;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProfileDetailActivity";
    private TextView FirstNameTextView, LastNameTextView, EmailTextView, PhoneNumberTextView, initialsTextView;
    private UserUtils userUtils;
    private ProgressBar progressBar;
    private String profileImage;
    private FirebaseFirestore db;
    private ImageView profileImageView;
    private String initials;
    private String userID, type;
    private Button deleteUserButton, deleteImage;


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
        type = intent.getStringExtra("type");
        FirstNameTextView = findViewById(R.id.viewFirstName);
        LastNameTextView = findViewById(R.id.viewLastName);
        EmailTextView = findViewById(R.id.viewEmail);
        PhoneNumberTextView = findViewById(R.id.viewPhoneNumber);
        initialsTextView = findViewById(R.id.initials);
        progressBar = findViewById(R.id.progressBar);
        deleteUserButton = findViewById(R.id.deleteProfile);
        deleteImage = findViewById(R.id.deleteImage);
        userUtils = new UserUtils();
        db = FirebaseFirestore.getInstance();
        profileImageView = findViewById(R.id.profileImage);
        fetchUserProfile();

        Log.d("access type", type);
        if (Objects.equals(type, "admin")) {
            deleteUserButton.setVisibility(View.VISIBLE);
            deleteUserButton.setOnClickListener(v -> {
                progressBar.setVisibility(View.VISIBLE);
                removeFromLists(userID, success -> {
                    if (success) {
                        deleteProfile(userID);
                    } else {
                        Toast.makeText(ProfileDetailActivity.this, "Failed to remove user from lists", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            });
            deleteImage.setVisibility(View.VISIBLE);
            deleteImage.setOnClickListener(v->{
                showRemoveProfilePictureDialog();
            });

        } else {
            deleteUserButton.setVisibility(View.GONE);
            deleteImage.setVisibility(View.GONE);
        }
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
                    if (Objects.equals(type, "admin")) deleteImage.setVisibility(View.VISIBLE);
                } else {

                    initialsTextView.setText(initials.toUpperCase());
                    profileImageView.setImageResource(R.drawable.circle_background);
                    initialsTextView.setVisibility(View.VISIBLE);
                    if (Objects.equals(type, "admin")) deleteImage.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void removeFromLists(String userID, CustomCallback callback) {
        db.collection("events")
                .get()
                .addOnCompleteListener(task-> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot eventDoc : task.getResult().getDocuments()) {
                            String eventId = eventDoc.getId();
                            db.collection("events").document(eventId).update(
                                    "waitingList", FieldValue.arrayRemove(userID),
                                    "selected", FieldValue.arrayRemove(userID),
                                    "registrants", FieldValue.arrayRemove(userID),
                                    "cancelledList", FieldValue.arrayRemove(userID)
                            );
                        }
                        callback.onComplete(true);
                    } else {
                        callback.onComplete(false);
                    }
                });

    }

    private void deleteProfile(String userID) {
        db.collection("users").document(userID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileDetailActivity.this, "Profile deleted", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                })
                .addOnFailureListener(e-> {
                    Toast.makeText(ProfileDetailActivity.this, "Failed to delete profile", Toast.LENGTH_SHORT).show();
                });
    }


    public interface CustomCallback {
        void onComplete(boolean success);
    }

    /**
     * Displays a confirmation dialog to remove the profile picture.
     */
    private void showRemoveProfilePictureDialog() {
        new AlertDialog.Builder(ProfileDetailActivity.this)
                .setTitle("Remove Profile Picture")
                .setMessage("Are you sure you want to remove the profile picture?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeProfilePicture();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Removes the profile picture by updating the view to display initials
     * and resetting the profileImageURL in Firebase Firestore.
     */
    private void removeProfilePicture() {
        initialsTextView.setText(initials.toUpperCase());
        profileImageView.setImageResource(R.drawable.circle_background);
        initialsTextView.setVisibility(View.VISIBLE);
        deleteImage.setVisibility(View.GONE);

        db.collection("users").document(userID)
                .update("profileImageURL", null)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileDetailActivity.this, "Profile picture removed successfully.", Toast.LENGTH_SHORT).show();
                    profileImage = null;
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileDetailActivity.this, "Failed to remove profile picture.", Toast.LENGTH_SHORT).show();
                });
    }

}