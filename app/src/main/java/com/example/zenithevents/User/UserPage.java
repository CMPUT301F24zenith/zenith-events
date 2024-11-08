package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.HelperClasses.ValidationUtils;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * UserPage handles displaying and updating the user's profile information.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.
 *
 * <p>This activity allows the user to view, edit, and save profile information such as their
 * first name, last name, email, and phone number. It also provides functionality for updating
 * the profile picture, either by selecting an image from the gallery or displaying initials if
 * no profile image is provided.
 *
 * <p>Usage Example:
 * <pre>
 * Intent intent = new Intent(context, UserPage.class);
 * startActivity(intent);
 * </pre>
 *
 * <p>Lifecycle Methods:
 * <ul>
 *   <li>{@link #onCreate(Bundle)} - Initializes UI components and loads the user's profile data.
 *   <li>{@link #fetchUserProfile()} - Retrieves the user's profile from the database and updates the UI.
 *   <li>{@link #saveProfile()} - Validates and saves the updated profile information.
 * </ul>
 *
 * Related Classes:
 * - {@link BitmapUtils} - Utility class for encoding and decoding bitmap images.
 * - {@link UserUtils} - Utility class for creating or updating the user's profile in the database.
 * - {@link ValidationUtils} - Utility class for validating user input such as email format.
 */
public class UserPage extends AppCompatActivity {

    private EditText editFirstName, editLastName, editEmail, editPhoneNumber;
    private Button btnSave, btnRemove;
    private ImageView profileImage;
    private TextView initialsTextView;
    private UserUtils userUtils;
    private Uri imageUri;
    private String existingProfileImageURL;
    private ProgressBar progressBar;
    String initials;
    FirebaseAuth mAuth;

    /**
     * Initializes the activity, setting up UI components and loading the user's profile data.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        userUtils = new UserUtils();
        mAuth = FirebaseAuth.getInstance();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        profileImage = findViewById(R.id.profileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        initialsTextView = findViewById(R.id.initials);
        progressBar = findViewById(R.id.progressBar);
        btnRemove = findViewById(R.id.btnRemove);

        fetchUserProfile();

        btnSave.setOnClickListener(v -> {
            saveProfile();
        });
        btnRemove.setOnClickListener(v -> {
            removeProfileImg();
        });

        profileImage.setOnClickListener(v -> openImagePicker());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Profile Did not update", Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens the device's gallery to allow the user to pick an image for their profile picture.
     */
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

    /**
     * Fetches the user's profile from the database and populates the UI fields with the retrieved data.
     */
    private void fetchUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        String deviceID = DeviceUtils.getDeviceID(this);
        userUtils.fetchUserProfile(deviceID, user -> {
            if (user != null) {
                editFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
                editLastName.setText(user.getLastName() != null ? user.getLastName() : "");
                editEmail.setText(user.getEmail() != null ? user.getEmail() : "");
                editPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");

                existingProfileImageURL = user.getProfileImageURL();
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                initials = getInitials(firstName, lastName);

                // Set image or initials if no profile image exists
                if (existingProfileImageURL != null && !existingProfileImageURL.isEmpty()) {

                    Bitmap decodedProfileImage = BitmapUtils.decodeBase64ToBitmap(existingProfileImageURL);
                    profileImage.setImageBitmap(decodedProfileImage);
                    initialsTextView.setVisibility(View.GONE);
                } else {

                    initialsTextView.setText(initials.toUpperCase());
                    profileImage.setImageResource(R.drawable.circle_background);
                    initialsTextView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    /**
     * Generates initials from the first and last names.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @return A string representing the initials.
     */
    private String getInitials(String firstName, String lastName) {
        String initials = "";
        if (!firstName.isEmpty()) initials += firstName.substring(0, 1);
        if (!lastName.isEmpty()) initials += lastName.substring(0, 1);
        return initials;
    }

    /**
     * Saves the user's profile by validating inputs and updating the database.
     * If an image is selected, it encodes and stores the image; otherwise, it saves existing data.
     */
    private void saveProfile() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "First name, last name, and email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        updatedUser.setDeviceID(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phoneNumber);
        if (imageUri != null){
            Bitmap bitmap = BitmapUtils.getBitmapFromUri(this, imageUri);
            if (bitmap != null) {
                String encodedImage = BitmapUtils.encodeBitmapToBase64(bitmap);
                updatedUser.setProfileImageURL(encodedImage);
            } else {
                Toast.makeText(this, "Failed to encode image.", Toast.LENGTH_SHORT).show();
            }

        }else {
            updatedUser.setProfileImageURL(existingProfileImageURL);
        }

        updateUserProfile(updatedUser);

    }

    /**
     * Updates the user's profile in the database and provides feedback on the success or failure of the operation.
     *
     * @param user The {@link User} object containing updated profile information.
     */
    private void updateUserProfile(User user) {
        userUtils.createOrUpdateUserProfile(user, success -> {
            if (success) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed. Try smaller image size", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Removes the profile image from the user's profile.
     */
    public void removeProfileImg(){
        initialsTextView.setText(initials.toUpperCase());
        profileImage.setImageResource(R.drawable.circle_background);
        initialsTextView.setVisibility(View.VISIBLE);
        imageUri = null;
        existingProfileImageURL = null;
    }
}
