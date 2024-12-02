package com.example.zenithevents.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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

/**
 * AdminFacilitiesFragment is a fragment responsible for displaying and managing
 * a list of facilities for the admin interface. It listens for real-time updates
 * using {@link FacilityUtils} and updates the UI accordingly.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class AdminFacilitiesFragment extends Fragment {
    private static final String TAG = "ViewFacilitiesAdminFragment";
    private ListView facilitiesListView;
    private FacilityArrayAdapter adapter;
    private List<Facility> facilityList = new ArrayList<>();

    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that this fragment's UI should be attached to, if applicable.
     * @param savedInstanceState A Bundle object containing the fragment's previously saved state, if any.
     * @return The root view of the fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_admin_facilities, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView}. Initializes the UI components and sets up
     * event listeners for real-time updates using {@link FacilityUtils}.
     *
     * @param view The View returned by {@link #onCreateView}.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Apply window insets to the root view
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        facilitiesListView = view.findViewById(R.id.listView);
        adapter = new FacilityArrayAdapter(requireContext(), facilityList);
        facilitiesListView.setAdapter(adapter);
        facilitiesListView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.list_layout_animation)
        );


        FacilityUtils.listenForFacilitiesChanges(facilities -> {
            if (facilities != null) {
                facilityList.clear();
                facilityList.addAll(facilities);
                adapter.notifyDataSetChanged();
                facilitiesListView.scheduleLayoutAnimation();
            } else {
                Log.e(TAG, "Failed to fetch facilities");
            }
        });
    }

    /**
     * Called when the view is being destroyed. Cleans up any resources or listeners set up
     * during the fragment's lifecycle, including stopping the listener for facility updates.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FacilityUtils.stopListeningForFacilities();
    }
}
