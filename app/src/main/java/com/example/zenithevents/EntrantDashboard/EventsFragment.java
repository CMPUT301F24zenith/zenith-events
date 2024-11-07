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



    }

