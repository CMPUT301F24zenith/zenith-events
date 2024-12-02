package com.example.zenithevents.HelperClasses;

import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for handling event data and listening to changes in Firestore's "events" collection.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class FirestoreEventsCollection {

    /**
     * Callback interface for retrieving a list of events from Firestore.
     */
    public interface FetchEventsCallback {
        /**
         * This method is called when the list of events is fetched from Firestore.
         *
         * @param eventList A list of events fetched from Firestore, or null if an error occurred.
         */
        void onCallback(List<Event> eventList);
    }

    private static ListenerRegistration listenerRegistration;

    /**
     * Listens for changes in the "events" collection in Firestore and notifies the provided callback with the list of events.
     *
     * @param callback The callback to be invoked when the list of events is retrieved or when an error occurs.
     */
    public static void listenForEventChanges(FetchEventsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("events")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    List<Event> eventList = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                    }
                    callback.onCallback(eventList);
                });
    }

    /**
     * Listens for changes in the "events" collection filtered by a specific "ownerFacility" user ID,
     * and notifies the provided callback with the list of events associated with that user.
     *
     * @param userId  The user ID (facility ID) to filter the events by.
     * @param callback The callback to be invoked when the list of events is retrieved or when an error occurs.
     */
    public static void listenForUserEventChanges(String userId, FetchEventsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("events")
                .whereEqualTo("ownerFacility", userId)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    List<Event> eventList = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                    }
                    callback.onCallback(eventList);
                });
    }

    /**
     * Listens for changes in the "events" collection filtered by a specific "ownerFacility" user ID and
     * a specific array type in the event data, and notifies the provided callback with the list of events.
     *
     * @param userId  The user ID (facility ID) to filter the events by.
     * @param arrayType The type of array (e.g., participants, organizers) to filter by.
     * @param callback The callback to be invoked when the list of events is retrieved or when an error occurs.
     */
    public static void listenForUserEventArrayChanges(String userId, String arrayType, FetchEventsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("events")
                .whereEqualTo("ownerFacility", userId)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    List<Event> eventList = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                    }
                    callback.onCallback(eventList);
                });
    }

    /**
     * Stops listening for changes in the "events" collection in Firestore.
     * This method should be called to stop receiving updates from Firestore when no longer needed.
     */
    public static void stopListeningForEventChanges() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
