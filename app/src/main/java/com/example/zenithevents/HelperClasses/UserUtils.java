package com.example.zenithevents.HelperClasses;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.zenithevents.Objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public void checkUserExists(UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        callback.onUserCheckComplete(document != null && document.exists());
                    } else {
                        callback.onUserCheckComplete(false); // Assume user doesn't exist in case of error
                    }
                });
    }


    public void createOrUpdateUserProfile(User user, UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
    }


    public void deleteUserProfile(UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))  // Deleted successfully
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));   // Deletion failed
    }


    public void fetchUserProfile(UserFetchCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get()
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


    public void updateUserField(String fieldName, Object value, UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .update(fieldName, value)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))  // Update successful
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));    // Update failed
    }


    public void checkAdminStatus(UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get()
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

    public void applyEvent(Context context, String eventId, UserExistenceCallback callback) {
        String deviceID = DeviceUtils.getDeviceID(context);

        db.collection("users").document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> waitingEvents = (List<String>) documentSnapshot.get("waitingEvents");
                        if (waitingEvents == null)
                            waitingEvents = new ArrayList<>();

                        if (!waitingEvents.contains(eventId)) {
                            waitingEvents.add(eventId);
                            db.collection("users").document(deviceID)
                                    .update("waitingEvents", waitingEvents)
                                    .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                                    .addOnFailureListener(e -> callback.onUserCheckComplete(false));
                        }  else {
                            waitingEvents.remove(eventId);
                            db.collection("users").document(deviceID)
                                    .update("waitingEvents", waitingEvents)
                                    .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                                    .addOnFailureListener(e -> callback.onUserCheckComplete(false));
                        }
                    } else {
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("deviceID", deviceID);
                        newUser.put("email", "mmngf@ggg.com");
                        newUser.put("entrantEvents", null);
                        newUser.put("waitingEvents", new ArrayList<>(List.of(eventId)));
                        newUser.put("firstName", "m");
                        newUser.put("lastName", "A");
                        newUser.put("myFacility", null);
                        newUser.put("phoneNumber", "1258");
                        newUser.put("profileImageURL", null);

                        db.collection("users").document(deviceID)
                                .set(newUser)
                                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
                    }
                })
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
    }

    // Helper method to convert a User object to a Map for Firestore
    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userID", user.getDeviceID());
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

    public void updateUserFields(Map<String, Object> fieldsToUpdate, UserExistenceCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .update(fieldsToUpdate)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))  // Update successful
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));    // Update failed
    }
}
