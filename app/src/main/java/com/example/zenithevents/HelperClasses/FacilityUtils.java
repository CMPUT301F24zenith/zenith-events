package com.example.zenithevents.HelperClasses;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Utility class for handling facility-related operations, including retrieving facility names from Firestore.
 * <p>
 * <b>Disclaimer:</b> The Javadocs for this class were generated with the assistance of AI.
 */
public class FacilityUtils {
    private FirebaseFirestore db;

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
}
