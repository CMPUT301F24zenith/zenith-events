package com.example.zenithevents.EntrantsList;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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

public class ListViewActivity extends AppCompatActivity {

    private ArrayList<User> dataList;
    private ListView entrantList;
    private EntrantArrayAdapter entrantAdapter;
    private FirebaseFirestore db;
    private String eventId;
    private String[] options = {"Waiting List", "Invited Users", "Registered Users", "Cancelled Users"};
    private TextView tvPrevious, tvCurrent, tvNext;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_list);

        db = FirebaseFirestore.getInstance();
        tvPrevious = findViewById(R.id.tvPrevious);
        tvCurrent = findViewById(R.id.CurrentSelection);
        tvNext = findViewById(R.id.tvNext);
        entrantList = findViewById(R.id.list_entrants);
        dataList = new ArrayList<>();

        this.eventId = getIntent().getStringExtra("eventId");
        if (this.eventId != null) {

            tvCurrent.setText(options[currentIndex]);

            tvPrevious.setOnClickListener(v -> {
                currentIndex = (currentIndex - 1 + options.length) % options.length;
                tvCurrent.setText(options[currentIndex]);
                switchList();
            });

            tvNext.setOnClickListener(v -> {
                currentIndex = (currentIndex + 1) % options.length;
                tvCurrent.setText(options[currentIndex]);
                switchList();
            });
        } else {
            Toast.makeText(this, "eventId is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchList() {
        switch (currentIndex) {
            case 0:
                entrantAdapter = new EntrantArrayAdapter(this, dataList, "WaitlistedEntrants", this.eventId);
                entrantList.setAdapter(entrantAdapter);
                showWaitingList();
                break;
            case 1:
                entrantAdapter = new EntrantArrayAdapter(this, dataList, "SampledEntrants", this.eventId);
                entrantList.setAdapter(entrantAdapter);
                showListSampled();
                break;
            case 2:
                entrantAdapter = new EntrantArrayAdapter(this, dataList, "EnrolledEntrants", this.eventId);
                entrantList.setAdapter(entrantAdapter);
                showListEnrolled();
                break;
            case 3:
                entrantAdapter = new EntrantArrayAdapter(this, dataList, "CancelledEntrants", this.eventId);
                entrantList.setAdapter(entrantAdapter);
                showListCancelled();
        }
    }

    private void showWaitingList() {
        db.collection("events")
                .document(this.eventId)
                .get()
                .addOnCompleteListener(documentSnapshotTask -> {
                    if (documentSnapshotTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();
                        if (documentSnapshot.exists()) {
                            dataList.clear();
                            List<String> enrolledEntrants = (List<String>) documentSnapshot.get("waitingList");
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
                        Toast.makeText(ListViewActivity.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }

    private void showListSampled() {
        db.collection("events")
                .document(this.eventId)
                .get()
                .addOnCompleteListener(documentSnapshotTask -> {
                    if (documentSnapshotTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();
                        if (documentSnapshot.exists()) {
                            dataList.clear();
                            List<String> selectedEntrants = (List<String>) documentSnapshot.get("selected");
                            assert selectedEntrants != null;
                            Log.d("FunctionCall", String.valueOf(selectedEntrants.size()));
                            int totalEntrants = selectedEntrants.size();
                            for (String id : selectedEntrants) {
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
                        Toast.makeText(ListViewActivity.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }

    private void showListEnrolled() {
        db.collection("events")
                .document(this.eventId)
                .get()
                .addOnCompleteListener(documentSnapshotTask -> {
                    if (documentSnapshotTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();
                        if (documentSnapshot.exists()) {
                            dataList.clear();
                            List<String> enrolledEntrants = (List<String>) documentSnapshot.get("registrants");
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
                        Toast.makeText(ListViewActivity.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }

    private void showListCancelled() {
        db.collection("events")
                .document(this.eventId)
                .get()
                .addOnCompleteListener(documentSnapshotTask -> {
                    if (documentSnapshotTask.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = documentSnapshotTask.getResult();
                        if (documentSnapshot.exists()) {
                            dataList.clear();
                            List<String> enrolledEntrants = (List<String>) documentSnapshot.get("cancelledList");
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
                        Toast.makeText(ListViewActivity.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }
}
