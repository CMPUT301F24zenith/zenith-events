package com.example.zenithevents.HelperClasses;

import android.content.Context;
import android.util.Log;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Notification;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
public class NotificationUtils {
    private FirebaseFirestore db;

    /**
     * Constructs an EventUtils instance with a Firestore instance.
     */
    public NotificationUtils() {
        db = FirebaseFirestore.getInstance();
    }

    public interface ExistenceCallback {
        void onComplete(boolean exists);
    }

    public void createNotification(Notification notif, ExistenceCallback existenceCallback) {
        db.collection("notifications")
                .document(notif.getEventId())
                .set(convertNotificationToMap(notif))
                .addOnSuccessListener(documentReference -> {
                    existenceCallback.onComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.d("FunctionCall", "Error adding notification: " + e.getMessage());
                    existenceCallback.onComplete(false);
                });
    }


    /**
     * Converts an Event object into a map representation suitable for storage in Firestore.
     *
     * <p>This method takes an Event object and maps its properties to a {@link Map}, with each
     * property represented as a key-value pair. This is useful for adding or updating event
     * data in Firestore. The method includes the event's ID, title, image URL, participant count,
     * waiting list, selected participants, registrants, owner facility ID, and the QR code hash.
     *
     * @param notif The Event object to convert into a map.
     * @return A map containing the event data, where each key corresponds to a field name in Firestore.
     */
    public static Map<String, Object> convertNotificationToMap(Notification notif) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("eventId", notif.getEventId());
        notificationMap.put("subject", notif.getSubject());
        notificationMap.put("message", notif.getMessage());
        notificationMap.put("entrants", notif.getEntrants());
        return notificationMap;
    }

}

