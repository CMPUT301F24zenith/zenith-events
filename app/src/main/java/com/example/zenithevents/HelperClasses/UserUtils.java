package com.example.zenithevents.HelperClasses;


import android.content.Context;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.zenithevents.Objects.User;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for user-related operations.
 */
public class UserUtils {

    private FirebaseFirestore db;

    public UserUtils() {
        db = FirebaseFirestore.getInstance();
    }

    // Callback interface for user existence check
    public interface UserExistenceCallback {
        void onUserCheckComplete(boolean exists);
    }

    // Callback interface for fetching user data
    public interface UserFetchCallback {
        void onUserFetchComplete(User user);
    }

    // Method to check if the user exists in Firestore
    public void checkUserExists(Context context, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        callback.onUserCheckComplete(document != null && document.exists());
                    } else {
                        callback.onUserCheckComplete(false); // Assume user doesn't exist in case of error
                    }
                });
    }

    // Method to create or update user profile in Firestore
    public void createOrUpdateUserProfile(Context context, User user, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID).set(user)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
    }

    // Method to delete a user profile from Firestore
    public void deleteUserProfile(Context context, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))  // Deleted successfully
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));   // Deletion failed
    }

    // Method to fetch the user profile from Firestore
    public void fetchUserProfile(Context context, UserFetchCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onUserFetchComplete(user);
                    } else {
                        callback.onUserFetchComplete(null);  // No user found
                    }
                })
                .addOnFailureListener(e -> callback.onUserFetchComplete(null));  // Fetch failed
    }

    // Method to update specific fields in a user profile
    public void updateUserField(Context context, String fieldName, Object value, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID)
                .update(fieldName, value)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))  // Update successful
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));    // Update failed
    }

    // Method to check if the user has admin status
    public void checkAdminStatus(Context context, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                        callback.onUserCheckComplete(isAdmin != null && isAdmin);
                    } else {
                        callback.onUserCheckComplete(false);
                    }
                })
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
    }

    // Helper method to convert a User object to a Map for Firestore
    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("deviceID", user.getDeviceID());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("entrantEvents", user.getEntrantEvents());
        userMap.put("profileImageURL", user.getProfileImageURL());
        userMap.put("wantsNotifs", user.getWantsNotifs());
        userMap.put("isAdmin", user.getAdmin());
        userMap.put("myFacility", user.getMyFacility());
        return userMap;
    }
}

