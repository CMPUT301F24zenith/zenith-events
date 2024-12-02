package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.User.CreateProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.zenithevents.Objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for managing user-related operations in the application. This includes methods for checking
 * user existence, creating/updating profiles, deleting profiles, retrieving user data, and updating specific fields
 * in Firestore.
 * <p>
 * <b>Disclaimer:</b> The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class UserUtils {

    private FirebaseFirestore db;

    /**
     * Constructs a new UserUtils instance and initializes the Firestore database reference.
     */
    public UserUtils() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for user existence check.
     */
    public interface UserExistenceCallback {
        /**
         * Invoked when the user existence check completes.
         *
         * @param exists {@code true} if the user exists; {@code false} otherwise.
         */
        void onUserCheckComplete(boolean exists);
    }

    public interface joinEventCallback {
        /**
         * Invoked when the user existence check completes.
         *
         * @param exists {@code true} if the user exists; {@code false} otherwise.
         */
        void onUserJoinComplete(int exists, Event event);
    }

    /**
     * Callback interface for fetching user data.
     */
    public interface UserFetchCallback {
        /**
         * Invoked when the user data fetch operation completes.
         *
         * @param user The fetched User object, or {@code null} if no user was found or an error occurred.
         */
        void onUserFetchComplete(User user);
    }


    /**
     * Creates or updates the profile for the currently authenticated user in Firestore.
     *
     * @param user     The User object containing user profile information.
     * @param callback The callback invoked upon completion with a boolean indicating success.
     */
    public void updateUserByObject(User user, UserExistenceCallback callback) {
        Log.d("FunctionCall", "if 1,1");

        db.collection("users").document(user.getDeviceID()).set(user)
                .addOnSuccessListener(aVoid -> callback.onUserCheckComplete(true))
                .addOnFailureListener(e -> callback.onUserCheckComplete(false));
    }

    /**
     * Fetches the user profile associated with the specified device ID.
     *
     * @param deviceID The ID of the device for which the user profile is retrieved.
     * @param callback The callback invoked upon completion with the fetched User object or {@code null} if not found.
     */
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


    /**
     * Applies or removes a user from the waiting list of an event. Adds the event to the waiting list if
     * not present, otherwise removes it.
     *
     * @param context   The application context.
     * @param deviceId  The ID of the user's device.
     * @param eventId   The ID of the event to apply or leave.
     * @param callback  The callback invoked upon completion with a boolean indicating success.
     */
    public void applyLeaveEvent(Context context, String deviceId, String eventId, joinEventCallback callback) {
        EventUtils eventUtils = new EventUtils();
        AtomicInteger flag = new AtomicInteger();
        db.collection("users").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        assert user.getWaitingEvents() != null && user.getCancelledEvents() != null &&
                                user.getSelectedEvents() != null && user.getRegisteredEvents() != null;

                        Log.d("FunctionCall", "Getting lists");


                        if (
                                !user.getWaitingEvents().contains(eventId) &&
                                !user.getSelectedEvents().contains(eventId) &&
                                !user.getCancelledEvents().contains(eventId) &&
                                !user.getRegisteredEvents().contains(eventId)
                        ) {
                            // implement the geolocation logic here. if user accepts then run the next line otherwise call return;
                            eventUtils.fetchEventById(eventId, event -> {
                                if (event.getHasGeolocation()) {
                                    Log.d("FunctionCall", "1111k.1");
                                    new android.app.AlertDialog.Builder(context)
                                            .setTitle("Geolocation Required")
                                            .setMessage("This event requires geolocation. Do you want to continue?")
                                            .setPositiveButton("Yes", (dialog, which) -> {
                                                user.getWaitingEvents().add(eventId);
                                                updateEventApplyLeave(deviceId, user, eventId, callback);
                                            })
                                            .setNegativeButton("No", (dialog, which) ->
                                                callback.onUserJoinComplete(-1, null)
                                            )
                                            .setOnCancelListener(e ->
                                                    callback.onUserJoinComplete(-1, null)
                                            )
                                            .show();
                                } else {
                                    user.getWaitingEvents().add(eventId);
                                    updateEventApplyLeave(deviceId, user, eventId, callback);
                                }
                            });
                        } else {
                            user.getWaitingEvents().remove(eventId);
                            user.getCancelledEvents().remove(eventId);
                            user.getSelectedEvents().remove(eventId);
                            user.getRegisteredEvents().remove(eventId);
                            updateEventApplyLeave(deviceId, user, eventId, callback);
                        }
                    } else {
                        Intent intent = new Intent(context, CreateProfileActivity.class);
                        intent.putExtra("type", "Join Event");
                        context.startActivity(intent);
                        callback.onUserJoinComplete(-1, null);
                    }
                }).addOnFailureListener(e -> {
                    Log.d("FunctionCall", "Firestore fetch error");
                    callback.onUserJoinComplete(-1, null);
                });
    }

    /**
     * Updates the user's status in an event by either adding them to the waiting list or
     * removing them from all event lists (selected, registrants, cancelled, waiting list)
     * depending on their current status. It also updates the user's data in the Firestore.
     *
     * @param deviceId The device ID of the user applying to leave or join the event.
     * @param user The user object containing updated user information.
     * @param eventId The ID of the event that the user is applying to leave or join.
     * @param callback The callback to notify once the operation is complete, with a result status.
     */
    public void updateEventApplyLeave(String deviceId, User user, String eventId, joinEventCallback callback) {
        EventUtils eventUtils = new EventUtils();
        db.collection("users").document(deviceId)
                .update(convertUserToMap(user))
                .addOnSuccessListener(aVoid -> {
                    Log.d("FunctionCall", "1212.1");
                    db.collection("events").document(eventId).get()
                            .addOnSuccessListener(documentSnapshot1 -> {
                                if (documentSnapshot1.exists()) {
                                    Event event = documentSnapshot1.toObject(Event.class);

                                    assert event.getWaitingList() != null && event.getCancelledList() != null &&
                                            event.getSelected() != null && event.getRegistrants() != null;

                                    if (!event.getWaitingList().contains(deviceId) &&
                                            !event.getRegistrants().contains(deviceId) &&
                                            !event.getCancelledList().contains(deviceId) &&
                                            !event.getSelected().contains(deviceId)
                                    ) {
                                        event.getWaitingList().add(deviceId);

                                        db.collection("events").document(eventId)
                                                .update(eventUtils.convertEventToMap(event))
                                                .addOnSuccessListener(aVoid1 -> callback.onUserJoinComplete(1, event))
                                                .addOnFailureListener(e -> callback.onUserJoinComplete(-1, null));

                                    } else {
                                        event.getWaitingList().remove(deviceId);
                                        event.getRegistrants().remove(deviceId);
                                        event.getCancelledList().remove(deviceId);
                                        event.getSelected().remove(deviceId);
                                        event.getUserLocations().remove(deviceId);

                                        db.collection("events").document(eventId)
                                                .update(eventUtils.convertEventToMap(event))
                                                .addOnSuccessListener(aVoid1 -> callback.onUserJoinComplete(0, event))
                                                .addOnFailureListener(e -> callback.onUserJoinComplete(-1, null));

                                    }
                                }
                            })
                            .addOnFailureListener(e -> callback.onUserJoinComplete(-1, null));
                }).addOnFailureListener(e -> callback.onUserJoinComplete(-1, null));
    }

    /**
     * Rejects an event invitation by removing the user's device ID from the selected events list
     * and adding it to the cancelled events list. It also updates the event's selected list
     * and adds the user's device ID to the event's cancelled list.
     *
     * @param deviceId The device ID of the user rejecting the invitation.
     * @param eventId The ID of the event being rejected.
     * @param callback The callback to notify once the process is complete.
     */
    public void rejectEvent(String deviceId, String eventId, joinEventCallback callback) {
        EventUtils eventUtils = new EventUtils();
        fetchUserProfile(deviceId, user -> {
            if (user == null) {
                callback.onUserJoinComplete(0, null);
                return;
            }
            user.getSelectedEvents().remove(eventId);
            user.getCancelledEvents().add(eventId);
            updateUserByObject(user, task -> {
                if (!task) {
                    callback.onUserJoinComplete(0, null);
                    return;
                }
                eventUtils.fetchEventById(eventId, event -> {
                    if (event == null) {
                        callback.onUserJoinComplete(0, null);
                        return;
                    }
                    event.getSelected().remove(deviceId);
                    event.getCancelledList().add(deviceId);
                    eventUtils.createUpdateEvent(event, task2 -> {
                        if (task2 == null) {
                            callback.onUserJoinComplete(0, null);
                        } else {
                            callback.onUserJoinComplete(1, event);
                        }
                    });
                });
            });
        });
    }

    /**
     * Accepts an event invitation by updating the user's and the event's status.
     * The method removes the deviceId from the selected list and adds it to the registrants list of the event.
     * It then updates the user's registered and selected events.
     *
     * @param deviceId The device ID of the user accepting the invitation.
     * @param eventId The ID of the event to which the invitation is being accepted.
     */
    public void acceptEventInvitation(String deviceId, String eventId) {
        EventUtils eventUtils = new EventUtils();
        eventUtils.fetchEventById(eventId, event -> {
            if (event != null) {
                event.getSelected().remove(deviceId);
                if (!event.getSelected().contains(deviceId)) event.getRegistrants().add(deviceId);
                Log.d("FunctionCall", "accepting... selected: " + event.getSelected().size());
                eventUtils.createUpdateEvent(event, res -> {
                    if (res != null) {
                        fetchUserProfile(deviceId, user -> {
                            if (user != null) {
                                Log.d("FunctionCall", "accepting... userId fetched: " + user.getDeviceID());
                                if (!user.getRegisteredEvents().contains(eventId)) user.getRegisteredEvents().add(eventId);
                                user.getSelectedEvents().remove(eventId);
                                updateUserByObject(user, updateUserCallback -> {
                                    if (updateUserCallback) {
                                        Log.d("FunctionCall", "invite accepted");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Converts a User object to a Map for easy storage in Firestore.
     *
     * @param user The User object to convert.
     * @return A map representing the user's data, compatible with Firestore.
     */
    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        if (user.getDeviceID() != null) userMap.put("deviceID", user.getDeviceID());
        if (user.getFirstName() != null) userMap.put("firstName", user.getFirstName());
        if (user.getLastName() != null) userMap.put("lastName", user.getLastName());
        if (user.getEmail() != null) userMap.put("email", user.getEmail());
        if (user.getPhoneNumber() != null) userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("profileImageURL", user.getProfileImageURL());
        if (user.getWantsNotifs() != null) userMap.put("wantsNotifs", user.getWantsNotifs());
        if (user.getIsAdmin() != null) userMap.put("isAdmin", user.getIsAdmin());
        if (user.getMyFacility() != null) userMap.put("myFacility", user.getMyFacility());
        if (user.getWaitingEvents() != null) userMap.put("waitingEvents", user.getWaitingEvents());
        if (user.getSelectedEvents() != null) userMap.put("selectedEvents", user.getSelectedEvents());
        if (user.getRegisteredEvents() != null) userMap.put("registeredEvents", user.getRegisteredEvents());
        if (user.getCancelledEvents() != null) userMap.put("cancelledEvents", user.getCancelledEvents());
        if (user.getAnonymousAuthID() != null) userMap.put("anonymousAuthID", user.getAnonymousAuthID());
        if (user.getWantsNotifs() != null) userMap.put("wantsNotifs", user.getWantsNotifs());

        return userMap;
    }
}
