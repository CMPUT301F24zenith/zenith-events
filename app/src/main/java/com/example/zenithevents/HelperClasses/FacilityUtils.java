package com.example.zenithevents.HelperClasses;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FacilityUtils {
    private FirebaseFirestore db;

    public FacilityUtils() {
        db = FirebaseFirestore.getInstance();
    }

    // Callback interface for user existence check
    public interface FacilityNameCallback {
        void onUserCheckComplete(String facilityName);
    }

    public void fetchFacilityName(String deviceId, FacilityNameCallback callback) {
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
