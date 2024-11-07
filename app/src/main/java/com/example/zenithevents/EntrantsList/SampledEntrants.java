package com.example.zenithevents.EntrantsList;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SampledEntrants extends AppCompatActivity {

    private ArrayList<User> dataList;
    private ListView entrantList;
    private EntrantArrayAdapter entrantAdapter;
    private FirebaseFirestore db;
    private String eventId;
    //use button to initialize intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_list);

        db = FirebaseFirestore.getInstance();
        entrantList = findViewById(R.id.list_entrants);
        dataList = new ArrayList<>();
        entrantAdapter = new EntrantArrayAdapter(this, dataList);
        entrantList.setAdapter(entrantAdapter);

        this.eventId = getIntent().getStringExtra("eventId");
        if (this.eventId != null) {
            showListEnrolled();
        } else {
            Toast.makeText(this, "eventId is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void showListEnrolled() {
        db.collection("events")
                .document(this.eventId)
                .collection("selected")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User entrant = document.toObject(User.class);
                            dataList.add(entrant);
                        }
                        entrantAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SampledEntrants.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }
}