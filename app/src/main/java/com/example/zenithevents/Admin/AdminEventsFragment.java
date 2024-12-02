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

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.FirestoreEventsCollection;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminEventsFragment is responsible for displaying and managing a list of events
 * for the admin view in the application. It listens for real-time updates from a Firestore
 * collection and reflects those changes in a list view with animations.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class AdminEventsFragment extends Fragment {
    private static final String TAG = "ViewEventsAdminFragment";
    private ListView eventsListView;
    private EventArrayAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that this fragment's UI should be attached to, if applicable.
     * @param savedInstanceState A Bundle object containing the fragment's previously saved state, if any.
     * @return The root view of the fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_events, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView}. Initializes the UI components and sets up
     * event listeners for real-time updates from Firestore.
     *
     * @param view The View returned by {@link #onCreateView}.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     */
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
        eventsListView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.list_layout_animation)
        );

        FirestoreEventsCollection.listenForEventChanges(events -> {
            if (events != null) {
                eventList.clear();
                eventList.addAll(events);
                adapter.notifyDataSetChanged();
                eventsListView.scheduleLayoutAnimation();
            } else {
                Log.e(TAG, "Failed to fetch events");
            }
        });
    }

    /**
     * Called when the view is being destroyed. Cleans up any resources or listeners set up
     * during the fragment's lifecycle.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirestoreEventsCollection.stopListeningForEventChanges();
    }
}
