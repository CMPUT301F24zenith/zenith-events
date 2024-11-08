package com.example.zenithevents.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zenithevents.EntrantDashboard.EventsFragment;
import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;

public class OrganizerPage extends AppCompatActivity {
    Button createEventButton;
    ArrayList<Event> organizerEvents;
    EventUtils eventUtils = new EventUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myEventsFragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(v -> {
            Event event = new Event();
            Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
            intent.putExtra("page_title", "Create Event");
            intent.putExtra("Event", event);
            startActivity(intent);
            finish();
        });

        Bundle args = new Bundle();
        args.putString("type", "organizer");
        loadFragment(new EventsFragment(), args);
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        // Replace the fragment in the fragmentContainer
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myEventsFragment, fragment);
        fragmentTransaction.commit();
    }
}
