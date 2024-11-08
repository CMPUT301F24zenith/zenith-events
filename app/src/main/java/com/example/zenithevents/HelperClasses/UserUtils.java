package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Events.CreationSuccessActivity;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.User.UserProfile;
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

    public void fetchUserProfile(String deviceID, UserFetchCallback callback) {
        db.collection("users").document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("FunctionCall", "displayingDetails2: ");
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onUserFetchComplete(user);
                    } else {
                        callback.onUserFetchComplete(null);  // No user found
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onUserFetchComplete(null);
                    Log.d("FunctionCall", "displayingDetails3: ");
                });  // Fetch failed
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

    public void applyLeaveEvent(Context context, String deviceId, String eventId, UserExistenceCallback callback) {
        db.collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> waitingEvents = (List<String>) documentSnapshot.get("waitingEvents");
                        List<String> rejectedEvents = (List<String>) documentSnapshot.get("cancelledEvents");
                        List<String> invitedEvents = (List<String>) documentSnapshot.get("selectedEvents");
                        List<String> acceptedEvents = (List<String>) documentSnapshot.get("registeredEvents");

                        assert waitingEvents != null || rejectedEvents != null ||
                                invitedEvents != null || acceptedEvents != null;

                        Log.d("FunctionCall", "Getting lists");

                        if ((!waitingEvents.contains(eventId) &&
                                !rejectedEvents.contains(eventId) &&
                                !invitedEvents.contains(eventId) &&
                                !acceptedEvents.contains(eventId)
                        )) {
                            waitingEvents.add(eventId);
                        } else {
                            waitingEvents.remove(eventId);
                            rejectedEvents.remove(eventId);
                            invitedEvents.remove(eventId);
                            acceptedEvents.remove(eventId);
                        }

                        db.collection("users").document(deviceId)
                                .update("waitingEvents", waitingEvents)
                                .addOnSuccessListener(aVoid -> {
                                    db.collection("events").document(eventId).get()
                                            .addOnSuccessListener(documentSnapshot1 -> {
                                                List<String> waitingList = (List<String>) documentSnapshot1.get("waitingList");
                                                assert waitingList != null;

                                                if (!waitingList.contains(deviceId)) {
                                                    waitingList.add(deviceId);
                                                } else {
                                                    waitingList.remove(deviceId);
                                                }

                                                db.collection("events").document(eventId)
                                                        .update("waitingList", waitingList)
                                                        .addOnSuccessListener(aVoid1 -> callback.onUserCheckComplete(true))
                                                        .addOnFailureListener(e -> callback.onUserCheckComplete(false));
                                            })
                                            .addOnFailureListener(e -> callback.onUserCheckComplete(false));
                                }).addOnFailureListener(e -> callback.onUserCheckComplete(false));
                    } else {
                        Log.d("FunctionCall", "aaa");
                        Intent intent = new Intent(context, CreateProfileActivity.class);
                        context.startActivity(intent);
                        callback.onUserCheckComplete(false);
                    }
                }).addOnFailureListener(e -> {
                    Log.d("FunctionCall", "Firestore fetch error");
                    callback.onUserCheckComplete(false);
                });
    }

    // Helper method to convert a User object to a Map for Firestore
    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("deviceID", user.getDeviceID());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("profileImageURL", user.getProfileImageURL());
        userMap.put("wantsNotifs", user.getWantsNotifs());
        userMap.put("isAdmin", user.getAdmin());
        userMap.put("myFacility", user.getMyFacility());
        userMap.put("waitingEvents", user.getWaitingEvents());
        userMap.put("selectedEvents", user.getSelectedEvents());
        userMap.put("registeredEvents", user.getRegisteredEvents());
        userMap.put("cancelledEvents", user.getCancelledEvents());
        userMap.put("anonymousAuthID", user.getAnonymousAuthID());
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
