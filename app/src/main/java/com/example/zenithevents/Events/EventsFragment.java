package com.example.zenithevents.Events;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment that displays a list of events based on the user's role and event type.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
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


    // TODO: Rename and change types and number of parameters
    /**
     * Creates a new instance of the EventsFragment.
     *
     * @param param1 The first parameter (unused).
     * @param param2 The second parameter (unused).
     * @return A new instance of EventsFragment.
     */
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState A Bundle containing the saved state of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /**
     * Inflates the fragment's layout and initializes its views.
     * Based on the event type, it fetches appropriate events from Firestore.
     *
     * @param inflater The LayoutInflater used to inflate the view.
     * @param container The parent container for the view.
     * @param savedInstanceState A Bundle containing the saved state of the fragment.
     * @return The inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventListView = view.findViewById(R.id.eventsListView);
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
                fetchEntrantWaitingEvents("waitingEvents");
            if (Objects.equals(type, "entrant-selected"))
                fetchEntrantWaitingEvents("selectedEvents");
            if (Objects.equals(type, "entrant-cancelled"))
                fetchEntrantWaitingEvents("cancelledEvents");
            if (Objects.equals(type, "entrant-accepted"))
                fetchEntrantWaitingEvents("registrantsEvents");
        }
        return view;
    }

    /**
     * Called after the view has been created, can be used for additional setup.
     *
     * @param view The view that was created.
     * @param savedInstanceState A Bundle containing the saved state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Fetches events for an entrant based on the provided event type (e.g., waiting, selected).
     *
     * @param eventTypes The type of events to fetch (e.g., "waitingEvents", "selectedEvents").
     */
    private void fetchEntrantWaitingEvents(String eventTypes) {
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
                            List<String> waitingEvents = (List<String>) userDocument.get(eventTypes);

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
