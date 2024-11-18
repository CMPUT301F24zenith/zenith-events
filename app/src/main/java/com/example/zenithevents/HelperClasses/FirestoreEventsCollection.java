package com.example.zenithevents.HelperClasses;

import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class FirestoreEventsCollection {

    public interface FetchEventsCallback {
        void onCallback(List<Event> eventList);
    }

    private static ListenerRegistration listenerRegistration;

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

    public static void stopListeningForEventChanges() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
