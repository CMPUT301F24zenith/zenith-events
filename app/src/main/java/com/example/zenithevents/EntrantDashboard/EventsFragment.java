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
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {

    private ListView eventListView;
    private EventArrayAdapter adapter;
    private List<Event> events;
    private static final String TAG = "EventsFragment";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("events");



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventListView = view.findViewById(R.id.ListView);
        events = new ArrayList<>();
        adapter = new EventArrayAdapter(requireContext(), events);
        eventListView.setAdapter(adapter);
//        addMockEventsOnce();
        fetchEvents();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add your fragment-specific code here

    }

    private void fetchEvents() {
        eventsRef.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e("EVENTS FRAGMENT","Error fetching events", e);
                Toast.makeText(getContext(), "Error fetching events", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshots != null) {
                List<Event> fetchedEvents = new ArrayList<>();
                for (QueryDocumentSnapshot document : snapshots) {
                    Event event = document.toObject(Event.class);
                    fetchedEvents.add(event);
                }
                events.clear();
                events.addAll(fetchedEvents);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addMockEventsOnce() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean hasAddedMockEvents = sharedPreferences.getBoolean("hasAddedMockEvents", false);

        if (!hasAddedMockEvents) {
            // Create two mock events without setting eventId yet
            Event event1 = new Event("Mock Event 1", null, "https://example.com/image1.jpg", 100);
            Event event2 = new Event("Mock Event 2", null, "https://example.com/image2.jpg", 150);

            // Add event1 to Firestore and set its eventId to the document ID
            eventsRef.add(event1)
                    .addOnSuccessListener(documentReference -> {
                        String documentId = documentReference.getId();
                        event1.setEventId(documentId); // Set eventId to the document ID
                        eventsRef.document(documentId).set(event1); // Update Firestore with eventId
                        Log.d(TAG, "Mock event 1 added successfully with eventId: " + documentId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding mock event 1", e);
                        Toast.makeText(getContext(), "Error adding mock event 1", Toast.LENGTH_SHORT).show();
                    });

            // Add event2 to Firestore and set its eventId to the document ID
            eventsRef.add(event2)
                    .addOnSuccessListener(documentReference -> {
                        String documentId = documentReference.getId();
                        event2.setEventId(documentId); // Set eventId to the document ID
                        eventsRef.document(documentId).set(event2); // Update Firestore with eventId
                        Log.d(TAG, "Mock event 2 added successfully with eventId: " + documentId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding mock event 2", e);
                        Toast.makeText(getContext(), "Error adding mock event 2", Toast.LENGTH_SHORT).show();
                    });

            // Update SharedPreferences to avoid adding them again
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasAddedMockEvents", true);
            editor.apply();
        }

    }


}