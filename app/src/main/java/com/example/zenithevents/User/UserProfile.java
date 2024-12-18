package com.example.zenithevents.User;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.HelperClasses.ValidationUtils;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * UserProfile handles displaying and updating the user's profile information.
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
 * Intent intent = new Intent(context, UserProfile.class);
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
public class UserProfile extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private EditText editFirstName, editLastName, editEmail, editPhoneNumber;
    private Button btnSave, btnRemove;
    private ImageView profileImage;
    private TextView initialsTextView;
    private UserUtils userUtils;
    private CheckBox notifsCheckBox;
    private boolean wantsNotifs;
    private Uri imageUri;
    private String existingProfileImageURL;
    private ProgressBar progressBar;
    String initials;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;

    /**
     * Initializes the activity, setting up UI components and loading the user's profile data.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userUtils = new UserUtils();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        profileImage = findViewById(R.id.profileImage);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
        initialsTextView = findViewById(R.id.initials);
        progressBar = findViewById(R.id.progressBar);
        btnRemove = findViewById(R.id.btnRemove);
        notifsCheckBox = findViewById(R.id.notifsCheckBox);

        fetchUserProfile();

        notifsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                wantsNotifs = true;
                checkAndRequestNotificationPermission();
            } else {
                wantsNotifs = false;
            }
        });

        btnSave.setOnClickListener(v -> saveProfile());
        btnRemove.setOnClickListener(v -> removeProfileImg());

        profileImage.setOnClickListener(v -> openImagePicker());
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
                    Log.d("UserProfile", "Image URI: " + imageUri);
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
                notifsCheckBox.setChecked(user.getWantsNotifs() != null ? user.getWantsNotifs() : false);

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

        String updatedProfileImageURL = existingProfileImageURL;

        if (imageUri != null) {
            Bitmap bitmap = BitmapUtils.getBitmapFromUri(this, imageUri);
            if (bitmap != null) {
                updatedProfileImageURL = BitmapUtils.encodeBitmapToBase64(bitmap);  // Encode and update URL
            } else {
                Toast.makeText(this, "Failed to encode image.", Toast.LENGTH_SHORT).show();
            }
        }

        updateUserProfile(DeviceUtils.getDeviceID(this), firstName, lastName, email, phoneNumber, updatedProfileImageURL);
    }



    /**
     * Updates the user's profile in Firestore with the provided information.
     * Only non-null fields in the {@link User} object are updated, allowing for partial updates.
     *
     * @param deviceID        The unique device ID of the user.
     * @param firstName       The user's first name.
     * @param lastName        The user's last name.
     * @param email           The user's email address.
     * @param phoneNumber     The user's phone number.
     * @param profileImageURL The URL of the user's profile image.
     */
    private void updateUserProfile(String deviceID, String firstName, String lastName, String email, String phoneNumber, String profileImageURL ){
        User updatedUser = new User();
        updatedUser.setDeviceID(deviceID);
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phoneNumber);
        updatedUser.setWantsNotifs(wantsNotifs);
        updatedUser.setProfileImageURL(profileImageURL);
        Map<String, Object> userData = UserUtils.convertUserToMap(updatedUser);
        db.collection("users")
                .document(deviceID)
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
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

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            } else {

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


}
