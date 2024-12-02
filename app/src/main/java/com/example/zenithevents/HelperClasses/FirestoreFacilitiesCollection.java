package com.example.zenithevents.HelperClasses;
import android.util.Log;

import com.example.zenithevents.Objects.Facility;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for handling facility data and listening to changes in Firestore's "facilities" collection.
 * This class was generated using OpenAI's ChatGPT.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class FirestoreFacilitiesCollection {
    /**
     * Callback interface for retrieving a list of facilities from Firestore.
     */
    public interface FetchFacilitiesCallback {
        /**
         * This method is called when the list of facilities is fetched from Firestore.
         *
         * @param facilityList A list of facilities fetched from Firestore, or null if an error occurred.
         */
        void onCallback(List<Facility> facilityList);
    }

    /**
     * Callback interface for retrieving a single facility from Firestore.
     */
    public interface FetchFacilityCallback {
        /**
         * This method is called when a single facility is fetched from Firestore.
         *
         * @param facility The facility fetched from Firestore, or null if an error occurred or the document does not exist.
         */
        void onCallback(Facility facility);
    }

    private static ListenerRegistration collectionListenerRegistration;
    private static ListenerRegistration documentListenerRegistration;

    /**
     * Listens for changes in the entire "facilities" collection in Firestore and notifies the provided callback with the list of facilities.
     *
     * @param callback The callback to be invoked when the list of facilities is retrieved or when an error occurs.
     */
    public static void listenForFacilityCollectionChanges(FetchFacilitiesCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        collectionListenerRegistration = db.collection("facilities")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Collection listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    List<Facility> facilityList = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Facility facility = document.toObject(Facility.class);
                            facilityList.add(facility);
                        }
                    }
                    callback.onCallback(facilityList);
                });
    }

    /**
     * Listens for changes in a specific facility document in the "facilities" collection by its ID,
     * and notifies the provided callback with the fetched facility data.
     *
     * @param facilityId The ID of the facility document to listen for changes.
     * @param callback The callback to be invoked when the facility data is retrieved or when an error occurs.
     */
    public static void listenForSpecificFacilityChanges(String facilityId, FetchFacilityCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        documentListenerRegistration = db.collection("facilities")
                .document(facilityId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Document listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Facility facility = documentSnapshot.toObject(Facility.class);
                        callback.onCallback(facility);
                    } else {
                        callback.onCallback(null); // Document does not exist
                    }
                });
    }

    /**
     * Stops listening for changes in the "facilities" collection in Firestore.
     * This method should be called to stop receiving updates from Firestore when no longer needed.
     */
    public static void stopListeningForFacilityCollectionChanges() {
        if (collectionListenerRegistration != null) {
            collectionListenerRegistration.remove();
            collectionListenerRegistration = null;
        }
    }

    /**
     * Stops listening for changes in a specific facility document in Firestore.
     * This method should be called to stop receiving updates for that particular document.
     */
    public static void stopListeningForSpecificFacilityChanges() {
        if (documentListenerRegistration != null) {
            documentListenerRegistration.remove();
            documentListenerRegistration = null;
        }
    }
}
