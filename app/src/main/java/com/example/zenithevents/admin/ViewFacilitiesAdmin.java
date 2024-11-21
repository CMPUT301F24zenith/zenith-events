package com.example.zenithevents.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.ArrayAdapters.FacilityArrayAdapter;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class ViewFacilitiesAdmin extends AppCompatActivity {
    private static final String TAG = "ViewFacilitiesAdmin";
    ListView facilitiesListView;
    private FacilityArrayAdapter adapter;
    private List<Facility> facilityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_facilities_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        facilitiesListView = findViewById(R.id.facilitiesListView);
        adapter = new FacilityArrayAdapter(this, facilityList);
        facilitiesListView.setAdapter(adapter);

        // Fetch and display facility data
        FacilityUtils.listenForFacilitiesChanges(facilities -> {
            if(facilities != null) {
                facilityList.clear();
                facilityList.addAll(facilities);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch facilities");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FacilityUtils.stopListeningForFacilities();
    }
}


