package com.example.zenithevents.HelperClasses;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Notification;
import android.os.AsyncTask;
import android.os.Message;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirestoreNotificationFunctions {
    private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "YOUR_SERVER_KEY_HERE"; // Replace with your FCM server key


    /*
     * This function was generated from ChatGpt
     */
    public static void subscribeToGlobalTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Subscribed to global topic");
                    } else {
                        System.err.println("Failed to subscribe: " + task.getException());
                    }
                });
    }
}
