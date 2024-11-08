package com.example.zenithevents.EntrantsList;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
            showListSelected();
        } else {
            Toast.makeText(this, "eventId is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void showListSelected() {
        db.collection("events")
                .document(this.eventId)
                .get()
                .addOnCompleteListener(documentSnapshotTask -> {
                    if (documentSnapshotTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();
                        if (documentSnapshot.exists()) {
                            dataList.clear();

                            List<String> enrolledEntrants = (List<String>) documentSnapshot.get("selected");
                            assert enrolledEntrants != null;
                            Log.d("FunctionCall", String.valueOf(enrolledEntrants.size()));

                            int totalEntrants = enrolledEntrants.size();
                            for (String id : enrolledEntrants) {
                                db.collection("users").document(id)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot1 -> {
                                            if (documentSnapshot1.exists()) {
                                                User user = documentSnapshot1.toObject(User.class);
                                                dataList.add(user);
                                            }

                                            if (dataList.size() == totalEntrants) {
                                                entrantAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(SampledEntrants.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }
}