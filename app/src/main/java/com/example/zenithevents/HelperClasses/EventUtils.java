package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zenithevents.EntrantsList.EnrolledEntrants;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.zenithevents.Objects.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for event-related operations.
 */
public class EventUtils {
    private FirebaseFirestore db;

    public EventUtils() {
        db = FirebaseFirestore.getInstance();
    }

    public interface EventUpdateCallback {
        void onEventUpdate(String eventId);
    }

    public interface EventUserListCallback {
        void onEventsFetchComplete(User user);
    }

    // Callback interface for event existence check
    public interface EventExistenceCallback {
        void onEventCheckComplete(boolean exists);
    }

    // Callback interface for fetching event data
    public interface EventFetchCallback {
        void onEventFetchComplete(Event event);
    }

    // Callback interface for multiple events
    public interface EventsFetchCallback {
        void onEventsFetchComplete(ArrayList<Event> events);
    }

    /**
     * Checks if an event exists in Firestore.
     */
    public void checkEventExists(String eventId, EventExistenceCallback callback) {
        db.collection("events").document(eventId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        callback.onEventCheckComplete(document != null && document.exists());
                    } else {
                        callback.onEventCheckComplete(false); // Assume event doesn't exist in case of error
                    }
                });
    }

    /**
     * Creates event if event is not in Firestore otherwise updates the event in Firestore.
     */
    public void createUpdateEvent(Context context, Event event, EventUpdateCallback callback) {
        Log.d("FunctionCall1", "" + event.getSelectedLimit());

        String deviceID = DeviceUtils.getDeviceID(context);
        event.setOwnerFacility(deviceID);

        if (event.getEventId() == null) {
            db.collection("events").add(event)
                    .addOnSuccessListener(ref -> {
                        String generatedId = ref.getId();
                        Log.d("Firestore", "Event added to database");

                        ref.update("eventId", generatedId)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Id updated");
                                callback.onEventUpdate(generatedId);
                            }).addOnFailureListener(e -> {
                                Log.d("Firestore", "Id failed to updated");
                                callback.onEventUpdate(null);
                            });
                    }).addOnFailureListener(e -> {
                        Log.w("Firestore", "Couldn't add event to database");
                        callback.onEventUpdate(null);
                    });
        } else {
            DocumentReference ref = db.collection("events").document(event.getEventId());
            ref.set(event, SetOptions.merge())
                    .addOnSuccessListener(v -> {
                        Log.d("Firestore", "Event updated to database");
                        callback.onEventUpdate(event.getEventId());
                    }).addOnFailureListener(e -> {
                        Log.w("Firestore", "Couldn't find Event Id in the database");
                        callback.onEventUpdate(null);
                    });
        }
    }

    /**
     * This function fetches all events which are owned by the given deviceId
     * @param context
     * @param deviceId
     * @param callback
     */
    public void fetchOrganizerEvents(Context context, String deviceId, EventsFetchCallback callback) {
        db.collection("events")
                .whereEqualTo("ownerFacility", deviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> organizerEvents = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Event event = document.toObject(Event.class);
                        Log.d("FunctionCall", "event fetched");
                        if (event != null) {
                            organizerEvents.add(event);
                        }
                    }

                    callback.onEventsFetchComplete(organizerEvents);
                }).addOnFailureListener(e -> {
                    Log.d("FunctionCall", "failed");
                    callback.onEventsFetchComplete(null);
                });
    }

    /**
     * Deletes an event from Firestore.
     */
    public void deleteEvent(String eventId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Fetches an event by its ID from Firestore.
     */
    public void fetchEventById(String eventId, EventFetchCallback callback) {
        db.collection("events").document(eventId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        callback.onEventFetchComplete(event);
                    } else {
                        callback.onEventFetchComplete(null);  // No event found
                    }
                })
                .addOnFailureListener(e -> callback.onEventFetchComplete(null));  // Fetch failed
    }

    /**
     * Fetches all events from Firestore.
     */
    public void fetchAllEvents(EventsFetchCallback callback) {
        db.collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Event event = document.toObject(Event.class);
                        events.add(event);
                    }
                    callback.onEventsFetchComplete(events);
                })
                .addOnFailureListener(e -> callback.onEventsFetchComplete(null));  // Fetch failed
    }

    /**
     * Updates a specific field of an event in Firestore.
     */
    public void updateEventField(String eventId, String fieldName, Object value, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update(fieldName, value)
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Updates multiple fields of an event in Firestore.
     */
    public void updateEventFields(String eventId, Map<String, Object> fieldsToUpdate, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update(fieldsToUpdate)
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Adds a user to an event's waiting list in Firestore.
     */
    public void addUserToWaitingList(String eventId, String userId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayUnion(userId))
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Removes a user from an event's waiting list in Firestore.
     */
    public void removeUserFromWaitingList(String eventId, String userId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayRemove(userId))
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Helper method to convert an Event object to a Map for Firestore.
     */
    public static Map<String, Object> convertEventToMap(Event event) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventId", event.getEventId());
        eventMap.put("eventTitle", event.getEventTitle());
        eventMap.put("imageUrl", event.getImageUrl());
        eventMap.put("numParticipants", event.getNumParticipants());
        eventMap.put("waitingList", event.getWaitingList());
        eventMap.put("selected", event.getSelected());
        eventMap.put("registrants", event.getRegistrants());
        eventMap.put("ownerFacility", event.getOwnerFacility());
        eventMap.put("QRCodeURL", event.getQRCodeHash());
        return eventMap;
    }

}

