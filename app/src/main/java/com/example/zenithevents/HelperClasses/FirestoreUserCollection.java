package com.example.zenithevents.HelperClasses;

import android.util.Log;

import com.example.zenithevents.Objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class FirestoreUserCollection {

    public interface FetchUsersCallback {
        void onCallback(List<User> userList);
    }

    private static ListenerRegistration listenerRegistration;


    public static void listenForUserChanges(FetchUsersCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("users")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }
                    List<User> userList = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                    }
                    callback.onCallback(userList);
                });
    }

    /*
     * This function was generated from ChatGpt
     */
    public static void listenForSpecificUserChanges(String userId, FetchUsersCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("users")
                .document(userId)  // Listen to a specific user document
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        List<User> userList = new ArrayList<>();
                        if (user != null) {
                            userList.add(user);  // Only add this specific user to the list
                        }
                        callback.onCallback(userList);  // Pass the list containing just one user
                    } else {
                        // If the user does not exist
                        callback.onCallback(null);
                    }
                });
    }

    public static void stopListeningForUserChanges() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
