package com.example.zenithevents.HelperClasses;

import android.util.Log;

import com.example.zenithevents.Objects.Notification;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationUtils {
    private FirebaseFirestore db;

    public NotificationUtils() {
        db = FirebaseFirestore.getInstance();
    }

    public interface FacilityObj {
        void onFacilityFetchComplete(Notification notifications);
    }

    public void fetchUserNotification(String deviceId, FacilityObj callback) {
        db.collection("notifications")
                .document(deviceId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Notification notification = documentSnapshot.toObject(Notification.class);
                        callback.onFacilityFetchComplete(notification);
                        Log.d("FunctionCall", "Notification: " + documentSnapshot.getData());
                    } else {
                        callback.onFacilityFetchComplete(null);
                        Log.d("FunctionCall", "No notification found for deviceId: " + deviceId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("FunctionCall", "Error getting notification", e);
                });
    }
}