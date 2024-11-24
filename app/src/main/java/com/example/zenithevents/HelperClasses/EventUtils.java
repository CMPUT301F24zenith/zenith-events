package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.zenithevents.Objects.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for handling event-related operations in Firestore.
 *
 * <p>This class provides methods for creating, updating, deleting, and retrieving event data
 * from Firestore. It also includes methods for managing event waiting lists and updating
 * specific fields in an event document. The Javadocs for this class was generated with
 * the assistance of an AI language model and may require review to ensure it meets your coding
 * standards and project requirements.
 */
public class EventUtils {
    private FirebaseFirestore db;

    /**
     * Constructs an EventUtils instance with a Firestore instance.
     */
    public EventUtils() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for updating events.
     */
    public interface EventUpdateCallback {
        void onEventUpdate(String eventId);
    }

    /**
     * Callback interface for checking event existence.
     */
    public interface EventExistenceCallback {
        void onEventCheckComplete(boolean exists);
    }

    /**
     * Callback interface for fetching a single event.
     */
    public interface EventFetchCallback {
        void onEventFetchComplete(Event event);
    }

    /**
     * Callback interface for fetching multiple events.
     */
    public interface EventsFetchCallback {
        void onEventsFetchComplete(ArrayList<Event> events);
    }

    /**
     * Checks if an event exists in Firestore.
     *
     * @param eventId  The unique identifier of the event to check.
     * @param callback Callback that provides a boolean indicating existence.
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

    public void createUpdateEvent(Context context, Event event, EventUpdateCallback callback) {
        Log.d("FunctionCall1", "" + event.getSelectedLimit());

        String deviceID = DeviceUtils.getDeviceID(context);
        event.setOwnerFacility(deviceID);
        createUpdateEvent(event, callback);
    }
    /**
     * Creates a new event or updates an existing event in Firestore.
     *
     * <p>If the event ID is null, a new event document is created. Otherwise, the specified
     * event is updated. If the event is created successfully, its ID is added to the document.
     *
     * @param event    The event to create or update in Firestore.
     * @param callback Callback to return the event ID after creation or update.
     */
    public void createUpdateEvent(Event event, EventUpdateCallback callback) {
        Log.d("FunctionCall", "updatingEvent1...");

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
     * Fetches all events created by a specific device (organizer).
     *
     * @param context  The application context.
     * @param deviceId The device ID of the organizer.
     * @param callback Callback to return a list of the organizer's events.
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
     *
     * @param eventId  The ID of the event to delete.
     * @param callback Callback to return success status after deletion.
     */
    public void deleteEvent(String eventId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Fetches an event by its unique ID from Firestore.
     *
     * @param eventId  The unique identifier of the event.
     * @param callback Callback to return the fetched event or null if not found.
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
     * Fetches all events stored in Firestore.
     *
     * @param callback Callback to return a list of all events.
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
     *
     * @param eventId   The unique identifier of the event.
     * @param fieldName The name of the field to update.
     * @param value     The new value for the specified field.
     * @param callback  Callback to return success status of the update operation.
     */
    public void updateEventField(String eventId, String fieldName, Object value, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update(fieldName, value)
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Updates multiple fields of an event in Firestore.
     *
     * @param eventId         The unique identifier of the event.
     * @param fieldsToUpdate  A map containing field names and their new values.
     * @param callback        Callback to return success status of the update operation.
     */
    public void updateEventFields(String eventId, Map<String, Object> fieldsToUpdate, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update(fieldsToUpdate)
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Adds a user to an event's waiting list in Firestore.
     *
     * @param eventId  The unique identifier of the event.
     * @param userId   The unique identifier of the user.
     * @param callback Callback to return success status of the operation.
     */
    public void addUserToWaitingList(String eventId, String userId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayUnion(userId))
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Removes a user from an event's waiting list in Firestore.
     *
     * @param eventId  The unique identifier of the event.
     * @param userId   The unique identifier of the user.
     * @param callback Callback to return success status of the operation.
     */
    public void removeUserFromWaitingList(String eventId, String userId, EventExistenceCallback callback) {
        db.collection("events").document(eventId)
                .update("waitingList", FieldValue.arrayRemove(userId))
                .addOnSuccessListener(aVoid -> callback.onEventCheckComplete(true))
                .addOnFailureListener(e -> callback.onEventCheckComplete(false));
    }

    /**
     * Converts an Event object into a map representation suitable for storage in Firestore.
     *
     * <p>This method takes an Event object and maps its properties to a {@link Map}, with each
     * property represented as a key-value pair. This is useful for adding or updating event
     * data in Firestore. The method includes the event's ID, title, image URL, participant count,
     * waiting list, selected participants, registrants, owner facility ID, and the QR code hash.
     *
     * @param event The Event object to convert into a map.
     * @return A map containing the event data, where each key corresponds to a field name in Firestore.
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
        eventMap.put("userLocations", event.getUserLocations());
        return eventMap;
    }
}

