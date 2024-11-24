package com.example.zenithevents.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.zenithevents.ArrayAdapters.FacilityArrayAdapter;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class AdminFacilitiesFragment extends Fragment {
    private static final String TAG = "ViewFacilitiesAdminFragment";
    private ListView facilitiesListView;
    private FacilityArrayAdapter adapter;
    private List<Facility> facilityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_admin_facilities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Apply window insets to the root view
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the ListView and adapter
        facilitiesListView = view.findViewById(R.id.listView);
        adapter = new FacilityArrayAdapter(requireContext(), facilityList);
        facilitiesListView.setAdapter(adapter);

        // Fetch and display facility data
        FacilityUtils.listenForFacilitiesChanges(facilities -> {
            if (facilities != null) {
                facilityList.clear();
                facilityList.addAll(facilities);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch facilities");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FacilityUtils.stopListeningForFacilities();
    }
}
