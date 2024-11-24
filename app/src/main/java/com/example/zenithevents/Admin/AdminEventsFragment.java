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

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.FirestoreEventsCollection;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class AdminEventsFragment extends Fragment {
    private static final String TAG = "ViewEventsAdminFragment";
    private ListView eventsListView;
    private EventArrayAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        eventsListView = view.findViewById(R.id.eventsListView);
        adapter = new EventArrayAdapter(requireContext(), eventList, "admin", null);
        eventsListView.setAdapter(adapter);

        FirestoreEventsCollection.listenForEventChanges(events -> {
            if (events != null) {
                eventList.clear();
                eventList.addAll(events);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch events");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirestoreEventsCollection.stopListeningForEventChanges();
    }
}
