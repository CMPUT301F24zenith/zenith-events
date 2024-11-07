package com.example.zenithevents.EntrantDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {

    private ListView eventListView;
    private EventArrayAdapter adapter;
    List<Event> events, waitingEventsList;
    EventUtils eventUtils;
    private static final String TAG = "EventsFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef;
    String type, deviceID;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventListView = view.findViewById(R.id.ListView);
        events = new ArrayList<>();
        waitingEventsList = new ArrayList<>();
        adapter = new EventArrayAdapter(requireContext(), events);
        eventListView.setAdapter(adapter);
        eventUtils = new EventUtils();
        Context context = getActivity();
        int[] counter = {0};

        if (getArguments() != null) {
            type = getArguments().getString("type");
            if (Objects.equals(type, "organizer")) {
                deviceID = DeviceUtils.getDeviceID(context);
                eventUtils.fetchOrganizerEvents(context, deviceID, fetchedOrganizerEvents -> {
                    if (fetchedOrganizerEvents != null) {
                        events.clear();
                        events.addAll(fetchedOrganizerEvents);
                        adapter.notifyDataSetChanged();
                        Log.d("Firestore", "Fetched: " + waitingEventsList.size());
                    }
                });
            }
            if (Objects.equals(type, "entrant-waiting"))
                fetchEntrantWaitingEvents();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add your fragment-specific code here

    }

    private void fetchEntrantWaitingEvents() {
        Context context = getActivity();
        String deviceID = DeviceUtils.getDeviceID(context);
        final int[] counter = {0};

        db.collection("users")
                .whereEqualTo("deviceID", deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null && !snapshots.isEmpty()) {
                            DocumentSnapshot userDocument = snapshots.getDocuments().get(0);
                            List<String> waitingEvents = (List<String>) userDocument.get("waitingEvents");

                            if (waitingEvents != null) {
                                for (String eventId : waitingEvents) {
                                    eventUtils.fetchEventById(eventId, event -> {
                                        waitingEventsList.add(event);
                                        counter[0]++;

                                        if (counter[0] == waitingEvents.size()) {
                                            events.clear();
                                            events.addAll(waitingEventsList);
                                            adapter.notifyDataSetChanged();
                                            Log.d("Firestore", "Fetched: " + waitingEventsList.size());
                                        }
                                    });
                                }

                            } else {
                                Log.d("Firestore", "No device has the same devideID");
                            }
                        } else {
                            Log.d("Firestore", "Failed to fetch data");
                        }
                    }
                });
    }
}
