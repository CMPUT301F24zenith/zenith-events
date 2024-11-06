package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class EventUtils {
    private FirebaseFirestore db;

    public EventUtils() {
        db = FirebaseFirestore.getInstance();
    }

    public interface EventUpdateCallback {
        void onEventUpdate(String eventId);
    }

    public void updateEvent(Context context, Event event, EventUtils.EventUpdateCallback callback) {
        Log.d("FunctionCall", "updateEvent");

        String deviceID = DeviceUtils.getDeviceID(context);
        event.setDeviceId(deviceID);

        if (event.getEventId() == null) {
            db.collection("events").add(event)
                    .addOnSuccessListener(ref -> {
                        Log.d("Firestore", "Event added to database");
                        callback.onEventUpdate(ref.getId());
                    }).addOnFailureListener(e -> {
                        Log.w("Firestore", "Couldn't add event to database");
                        callback.onEventUpdate(null);
                    });
        } else {
            DocumentReference ref = db.collection("events").document(event.getEventId());
            ref.set(event, SetOptions.merge())
                    .addOnSuccessListener(v -> {
                        Log.d("Firestore", "Event updated to database");
                        callback.onEventUpdate(ref.getId());
                    }).addOnFailureListener(e -> {
                        Log.w("Firestrore", "Couldnt find Event Id in the database");
                        callback.onEventUpdate(null);
                    });
        }
    }
}