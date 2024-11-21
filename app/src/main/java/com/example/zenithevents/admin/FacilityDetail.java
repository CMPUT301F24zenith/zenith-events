package com.example.zenithevents.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.ArrayAdapters.EventArrayAdapter;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class FacilityDetail extends AppCompatActivity {
    private static final String TAG = "FacilityDetail";
    private TextView facilityName, facilityEmail, facilityPhoneNumber;
    private ListView facilityEventsListView;
    private Button deleteFacilityButton;
    private EventArrayAdapter eventArrayAdapter;
    private List<Event> eventList = new ArrayList<>();
    EventUtils eventUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facility_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left + v.getPaddingLeft(),
                    systemBars.top + v.getPaddingTop(),
                    systemBars.right + v.getPaddingRight(),
                    systemBars.bottom + v.getPaddingBottom()
            );
            return insets;
        });
        eventUtils = new EventUtils();



        facilityName = findViewById(R.id.facilityName);
        facilityEmail = findViewById(R.id.facilityEmail);
        facilityPhoneNumber = findViewById(R.id.facilityPhoneNumber);
        facilityEventsListView = findViewById(R.id.facilityEventsListView);

        eventArrayAdapter = new EventArrayAdapter(this, eventList);
        facilityEventsListView.setAdapter(eventArrayAdapter);

        facilityEventsListView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_animation)
        );
        Intent intent = getIntent();
        String facilityID = intent.getStringExtra("facilityID");


        eventUtils.fetchOrganizerEvents(this, facilityID, events -> {
            if (events != null) {
                eventList = events;
                eventArrayAdapter.clear();
                eventArrayAdapter.addAll(eventList);
                eventArrayAdapter.notifyDataSetChanged();
                facilityEventsListView.scheduleLayoutAnimation();
            }
        });

        facilityName.setText(intent.getStringExtra("facilityName"));
        facilityEmail.setText(intent.getStringExtra("facilityEmail"));
        facilityPhoneNumber.setText(intent.getStringExtra("facilityPhoneNumber"));

    }


}