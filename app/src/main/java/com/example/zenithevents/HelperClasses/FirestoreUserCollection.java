package com.example.zenithevents.HelperClasses;

import android.util.Log;

import com.example.zenithevents.Objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for handling user data and listening to changes in Firestore's "users" collection.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class FirestoreUserCollection {

    /**
     * Callback interface for retrieving a list of users from Firestore.
     */
    public interface FetchUsersCallback {
        /**
         * This method is called when the list of users is fetched from Firestore.
         *
         * @param userList A list of users fetched from Firestore, or null if an error occurred.
         */
        void onCallback(List<User> userList);
    }

    private static ListenerRegistration listenerRegistration;

    /**
     * Listens for changes in the "users" collection in Firestore and notifies the provided callback with the list of users.
     *
     * @param callback The callback to be invoked when the list of users is retrieved or when an error occurs.
     */
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

    /**
     * Listens for changes in a specific user document by ID in the "users" collection and notifies the provided callback with the user data.
     * This function was generated using OpenAI's ChatGPT.
     *
     * @param userId The ID of the user document to listen for changes.
     * @param callback The callback to be invoked when the user data is retrieved or when an error occurs.
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

    /**
     * Stops listening for changes in the "users" collection in Firestore.
     * This method should be called to stop receiving updates from Firestore when no longer needed.
     */
    public static void stopListeningForUserChanges() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
