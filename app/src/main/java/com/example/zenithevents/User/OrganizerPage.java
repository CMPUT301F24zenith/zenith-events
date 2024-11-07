package com.example.zenithevents.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.Events.CreateEventPage;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

public class OrganizerPage extends AppCompatActivity {
    Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_main);
        createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(v -> {
            Log.d("FunctionCall", "t");
            Event event = new Event();
            Log.d("FunctionCall", "t");
            Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
            intent.putExtra("page_title", "Create Event");
            intent.putExtra("Event", event);
            startActivity(intent);
        });
    }
}
