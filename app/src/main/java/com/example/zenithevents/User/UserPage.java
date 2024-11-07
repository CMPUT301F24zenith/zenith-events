package com.example.zenithevents.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class UserPage extends AppCompatActivity {

    private EditText editFirstName, editLastName, editEmail, editPhoneNumber;
    private Button btnSave, btnBack;
    private ImageView profileImage;
    private TextView initialsTextView;
    private UserUtils userUtils;
    private Uri imageUri;
    private String existingProfileImageURL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        userUtils = new UserUtils();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        profileImage = findViewById(R.id.profileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        initialsTextView = findViewById(R.id.initials);

        fetchUserProfile();
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveProfile());

        profileImage.setOnClickListener(v -> openImagePicker());

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Log.d("UserPage", "Image URI: " + imageUri);
                    profileImage.setImageURI(imageUri);
                    initialsTextView.setVisibility(View.GONE);
                }
            }
    );

    private void fetchUserProfile() {
        userUtils.fetchUserProfile(user -> {
            if (user != null) {
                editFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
                editLastName.setText(user.getLastName() != null ? user.getLastName() : "");
                editEmail.setText(user.getEmail() != null ? user.getEmail() : "");
                editPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");

                existingProfileImageURL = user.getProfileImageURL();
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();

                // Set image or initials if no profile image exists
                if (existingProfileImageURL != null && !existingProfileImageURL.isEmpty()) {
                    Glide.with(this)
                            .load(existingProfileImageURL)
                            .placeholder(R.drawable.circle_background)
                            .error(R.drawable.circle_background)
                            .circleCrop()
                            .into(profileImage);
                    initialsTextView.setVisibility(View.GONE);
                } else {
                    String initials = getInitials(firstName, lastName);
                    initialsTextView.setText(initials);
                    profileImage.setImageResource(R.drawable.circle_background);
                    initialsTextView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getInitials(String firstName, String lastName) {
        String initials = "";
        if (!firstName.isEmpty()) initials += firstName.substring(0, 1);
        if (!lastName.isEmpty()) initials += lastName.substring(0, 1);
        return initials;
    }

    private void saveProfile() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "First name, last name, and email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phoneNumber);

        if (imageUri != null) {
            if (existingProfileImageURL != null && !existingProfileImageURL.isEmpty()) {
                FirebaseStorage.getInstance().getReferenceFromUrl(existingProfileImageURL).delete()
                        .addOnSuccessListener(aVoid -> uploadImageAndUpdateProfile(updatedUser))
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to delete old image.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                uploadImageAndUpdateProfile(updatedUser);
            }
        } else {
            updateUserProfile(updatedUser);
        }
    }

    private void uploadImageAndUpdateProfile(User updatedUser) {
        if (imageUri == null) {
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
            Log.e("UserPage", "Image URI is null");
            return;
        }

        Log.d("UserPage", "Image URI: " + imageUri.toString());

        // Verify if URI is accessible
        try (InputStream stream = getContentResolver().openInputStream(imageUri)) {
            if (stream == null) {
                Toast.makeText(this, "Failed to open image stream.", Toast.LENGTH_SHORT).show();
                Log.e("UserPage", "Failed to open image stream for URI: " + imageUri.toString());
                return;
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error accessing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("UserPage", "Error accessing image stream: " + e.getMessage());
            return;
        }

        // Use .child() to create a clean, readable path structure
        String uniqueFileName = UUID.randomUUID().toString();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(uniqueFileName);

        Log.d("UserPage", "Uploading to Firebase Storage path: images/" + uniqueFileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d("UserPage", "Upload succeeded");
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                updatedUser.setProfileImageURL(uri.toString());
                                updateUserProfile(updatedUser);
                                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                                Log.d("UserPage", "Image download URL: " + uri.toString());
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                                Log.e("UserPage", "Failed to get download URL: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("UserPage", "Image upload failed: " + e.getMessage());
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    private void updateUserProfile(User user) {
        userUtils.createOrUpdateUserProfile(user, success -> {
            if (success) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
