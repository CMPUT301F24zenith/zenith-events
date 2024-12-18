package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * This method is used to create or update an event. It first sets the owner facility of the event
     * based on the device ID and then calls the internal method to create or update the event in the database.
     *
     * @param context The context of the caller, used to access device-specific information such as the device ID.
     * @param event The event object that contains details of the event to be created or updated.
     * @param callback The callback to handle the result of the create or update operation.
     */
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
     * @param deviceId The device ID of the organizer.
     * @param callback Callback to return a list of the organizer's events.
     */
    public void fetchFacilityEvents(String deviceId, EventsFetchCallback callback) {
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
                    Log.d("FunctionCall", "SIZE:: " + organizerEvents.size() + ":: ID:: " + deviceId);
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
    public void removeEvent(String eventId, EventExistenceCallback callback) {
        db.collection("users").get().addOnCompleteListener(task-> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DocumentSnapshot userDoc : task.getResult().getDocuments()) {
                    db.collection("users").document(userDoc.getId()).update(
                            "waitingEvents", FieldValue.arrayRemove(eventId),
                            "selectedEvents", FieldValue.arrayRemove(eventId),
                            "registeredEvents", FieldValue.arrayRemove(eventId),
                            "cancelledEvents", FieldValue.arrayRemove(eventId)
                    );
                }
                db.collection("events").document(eventId).delete()
                        .addOnSuccessListener(aVoid->callback.onEventCheckComplete(true))
                        .addOnFailureListener(e->callback.onEventCheckComplete(false));
            } else {
                callback.onEventCheckComplete(false);
            }
        });
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
        eventMap.put("eventTitle", event.getEventName());
        eventMap.put("imageUrl", event.getImageUrl());
        eventMap.put("numParticipants", event.getNumParticipants());
        eventMap.put("waitingList", event.getWaitingList());
        eventMap.put("selected", event.getSelected());
        eventMap.put("registrants", event.getRegistrants());
        eventMap.put("cancelledList", event.getCancelledList());
        eventMap.put("ownerFacility", event.getOwnerFacility());
        eventMap.put("QRCodeURL", event.getQRCodeHash());
        eventMap.put("userLocations", event.getUserLocations());
        return eventMap;
    }
}

