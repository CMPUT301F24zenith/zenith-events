package com.example.zenithevents.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.FirestoreEventsCollection;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO project part 4
 * xml files in progress - not used in the app
 */
public class ViewEventsAdmin extends AppCompatActivity {
    private static final String TAG = "ViewEventsAdmin";
    ListView eventsListView;
    private EventArrayAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_events_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        eventsListView = findViewById(R.id.eventsListView);
        adapter = new EventArrayAdapter(this, eventList, "admin", null);
        eventsListView.setAdapter(adapter);

        // Fetch and display event data
        FirestoreEventsCollection.listenForEventChanges(events -> {
            if(events != null) {
                eventList.clear();
                eventList.addAll(events);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch events");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirestoreEventsCollection.stopListeningForEventChanges();
    }
}