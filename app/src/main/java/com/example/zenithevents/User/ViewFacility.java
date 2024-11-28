package com.example.zenithevents.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Activity to view a facility's details.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class ViewFacility extends AppCompatActivity {

    private TextView facilityName;
    private TextView facilityPhone;
    private TextView facilityEmail;
    private Button editButton;
    private FirebaseFirestore db;
    private String deviceId;
    private BarChart eventBarChart;

    /**
     * Called when the activity is created. Initializes the UI elements and loads the facility data.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_facility);

        db = FirebaseFirestore.getInstance();
//        deviceId = DeviceUtils.getDeviceID(this);
        deviceId = getIntent().getStringExtra("deviceId");
        facilityName = findViewById(R.id.namefield);
        facilityPhone = findViewById(R.id.phonefield);
        facilityEmail = findViewById(R.id.emailfield);
        editButton = findViewById(R.id.editbuttonfield);
        eventBarChart = findViewById(R.id.eventBarChart);

        if (deviceId != null) {
            showFacilityData(deviceId);
            loadEventData(deviceId);
        } else {
            Toast.makeText(this, "Error in getting Facility", Toast.LENGTH_SHORT).show();
            finish();
        }

        showFacilityData(deviceId);

        editButton.setOnClickListener(v-> {
            Intent editIntent = new Intent(ViewFacility.this, CreateFacility.class);
            editIntent.putExtra("deviceId", deviceId);
            startActivity(editIntent);
            finish();
        });

    }

    /**
     * Loads and displays the facility's data from Firestore using the device ID.
     *
     * @param deviceId The device ID to fetch the facility details.
     */
    private void showFacilityData(String deviceId) {
        DocumentReference facilityRef = db.collection("facilities").document(deviceId);
        facilityRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Facility facility = documentSnapshot.toObject(Facility.class);
                if (facility != null) {
                    facilityName.setText("Name: " + facility.getNameOfFacility());
                    facilityPhone.setText("Phone: " + facility.getPhoneOfFacility());
                    facilityEmail.setText("Email: " + facility.getEmailOfFacility());
                }
            } else {
                Toast.makeText(this, "Facility not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Cannot load Facility", Toast.LENGTH_SHORT).show());
    }




//    private void loadEventData(String deviceId) {
//        db.collection("facilities").document(deviceId).collection("events")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        List<Event> events = new ArrayList<>();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Event event = document.toObject(Event.class);
//                            events.add(event);
//                        }
//                        displayEventDataInChart(events);
//                    } else {
//                        Toast.makeText(ViewFacility.this, "Failed to load events", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void displayEventDataInChart(List<Event> events) {
//        List<BarEntry> entries = new ArrayList<>();
//        for (int i = 0; i < events.size(); i++) {
//            // Assuming Event has a method getNumParticipants() that returns the number of enrolled participants
//            entries.add(new BarEntry(i, events.get(i).getNumParticipants()));
//        }
//
//        BarDataSet dataSet = new BarDataSet(entries, "Enrolled Participants");
//        BarData data = new BarData(dataSet);
//        eventBarChart.setData(data);
//        eventBarChart.invalidate(); // Refresh the chart
//    }

    PieChart pieChart = findViewById(R.id.pieChart);

    // Create a list of Pie Entries (one for each event)
    List<PieEntry> entries = new ArrayList<>();
entries.add(new PieEntry(30f, "Event 1"));
entries.add(new PieEntry(50f, "Event 2"));
entries.add(new PieEntry(20f, "Event 3"));

    // Create a PieDataSet with the entries
    PieDataSet dataSet = new PieDataSet(entries, "Event Participation");

// Customize the colors of the chart
dataSet.setColors(Color.RED, Color.GREEN, Color.BLUE);

    // Create PieData object and assign it to the chart
    PieData pieData = new PieData(dataSet);
pieChart.setData(pieData);

// Customize chart appearance
pieChart.setUsePercentValues(true);
pieChart.getDescription().setEnabled(false);
pieChart.setCenterText("Events");
pieChart.setCenterTextSize(20f);

// Refresh chart
pieChart.invalidate();

}
