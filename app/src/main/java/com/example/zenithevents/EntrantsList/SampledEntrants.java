package com.example.zenithevents.EntrantsList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Activity that displays a list of sampled entrants for a specific event.
 * <p>Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.</p>
 */
public class SampledEntrants extends AppCompatActivity {

    private ArrayList<User> dataList;
    private ListView entrantList;
    private EntrantArrayAdapter entrantAdapter;
    private FirebaseFirestore db;
    private String eventId;
    TextView myEventsTitle, noEventsTextView;

    /**
     * Called when the activity is created. Initializes the view and fetches the sampled entrants for the event.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_list);

        db = FirebaseFirestore.getInstance();
        entrantList = findViewById(R.id.list_entrants);
        noEventsTextView = findViewById(R.id.noEvents);

        myEventsTitle = findViewById(R.id.myEventsTitle);
        myEventsTitle.setText("Sampled Entrants");

        dataList = new ArrayList<>();
        this.eventId = getIntent().getStringExtra("eventId");
        if (this.eventId != null) {
            entrantAdapter = new EntrantArrayAdapter(this, dataList, "SampledEntrants", this.eventId);
            entrantList.setAdapter(entrantAdapter);
            showListSampled();
        } else {
            Toast.makeText(this, "eventId is missing", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetches the sampled entrants for the specified event from Firestore and updates the list view.
     */
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
                            if (totalEntrants == 0) {
                                noEventsTextView.setVisibility(View.VISIBLE);
                            } else {
                                noEventsTextView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(SampledEntrants.this, "Failed to load entrants", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e-> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());
    }
}