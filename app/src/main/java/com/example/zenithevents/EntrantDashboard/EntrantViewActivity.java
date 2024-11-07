package com.example.zenithevents.EntrantDashboard;

import android.os.Bundle;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;


import com.example.zenithevents.R;

public class EntrantViewActivity extends AppCompatActivity {
    private static final String TAG = "EntrantViewActivity";
    Button events, myEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrant_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        events = findViewById(R.id.btnEvents);
        myEvents = findViewById(R.id.btnMyEvents);

        Bundle args = new Bundle();
        args.putString("type", "organizer");
        loadFragment(new EventsFragment(), args);

        // Set up button clicks to load different fragments
        events.setOnClickListener(v -> loadFragment(new EventsFragment(), args));
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        // Replace the fragment in the fragmentContainer
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment); // Updated ID to fragmentContainer
        fragmentTransaction.commit();
    }
}
