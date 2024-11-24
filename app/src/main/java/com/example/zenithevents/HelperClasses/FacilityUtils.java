package com.example.zenithevents.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling facility-related operations, including retrieving facility names from Firestore.
 * <p>
 * <b>Disclaimer:</b> The Javadocs for this class were generated with the assistance of AI.
 */
public class FacilityUtils {
    private FirebaseFirestore db;
    private static ListenerRegistration listenerRegistration;
    EventUtils eventUtils = new EventUtils();

    /**
     * Constructs a new FacilityUtils instance and initializes the Firestore database reference.
     */
    public FacilityUtils() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for facility name retrieval.
     */
    public interface FacilityNameCallback {
        /**
         * Invoked when the facility name retrieval completes.
         *
         * @param facilityName The name of the facility if retrieval is successful, or {@code null} if not found or an error occurred.
         */
        void onUserCheckComplete(String facilityName);
    }

    /**
     * Retrieves the name of a facility based on the provided device ID. This method performs an asynchronous Firestore lookup and returns
     * the result via a callback.
     *
     * @param deviceId The ID of the device associated with the facility.
     * @param callback The callback invoked upon completion of the retrieval, with the facility name as the parameter.
     */
    public void fetchFacilityName(String deviceId, FacilityNameCallback callback) {
        if (deviceId == null) {
            callback.onUserCheckComplete(null);
            return;
        }
        db.collection("facilities").document(deviceId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String facilityName = document.getString("nameOfFacility");
                        callback.onUserCheckComplete(facilityName);
                    } else {
                        callback.onUserCheckComplete(null); // Assume user doesn't exist in case of error
                    }
                });
    }

    public interface FirestoreFacilitiesCallback{
        void onCallback(List<Facility> facilityList);
    }

    public static void listenForFacilitiesChanges(FirestoreFacilitiesCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listenerRegistration = db.collection("facilities")
                .addSnapshotListener((querySnapShot, e) ->{
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e);
                        callback.onCallback(null);
                        return;
                    }
                    List<Facility> facilityList = new ArrayList<>();
                    if(querySnapShot != null) {
                        for (DocumentSnapshot document : querySnapShot.getDocuments()) {
                            Facility facility = document.toObject(Facility.class);
                            facilityList.add(facility);
                        }
                    }
                    callback.onCallback(facilityList);
                });
    }

    public void deleteFacility(Context context, String deviceId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("facilities").document(deviceId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("FunctionCall", "DELETING FACILITY:: ID:: " + deviceId);

                    Toast.makeText(context, "Deleting facility and its events", Toast.LENGTH_SHORT).show();
                    eventUtils.fetchFacilityEvents(deviceId, events -> {
                        if (events != null) {
                            if (events.size() == 0) {
                                ((Activity) context).finish();
                            }

                            for (Event event : events) {
                                eventUtils.removeEvent(event.getEventId(), isDeleted -> {
                                    Toast.makeText(context, "Facility deleted", Toast.LENGTH_SHORT).show();
                                    ((Activity) context).finish();
                                    if (!isDeleted) {
                                        Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                        }
                    });
                })
                .addOnFailureListener(e-> {
                    Toast.makeText(context, "Facility failed to delete", Toast.LENGTH_SHORT).show();
                });
    }

    public static void stopListeningForFacilities() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }




}
