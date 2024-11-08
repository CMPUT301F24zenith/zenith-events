package com.example.zenithevents.EntrantDashboard;

import android.content.Intent;
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


import com.example.zenithevents.MainActivity;
import com.example.zenithevents.QRCodes.QRScannerActivity;
import com.example.zenithevents.R;

public class EntrantViewActivity extends AppCompatActivity {
    private static final String TAG = "EntrantViewActivity";
    Button scanQRButton, btnMyWaitingEvents, btnMyEvents, btnCancelledEvents, btnSelectedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrant_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityEntrant), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnMyWaitingEvents = findViewById(R.id.btnMyWaitingEvents);
        scanQRButton = findViewById(R.id.scanQRButton);
        btnMyEvents = findViewById(R.id.btnMyEvents);
        btnCancelledEvents = findViewById(R.id.btnCancelledEvents);
        btnSelectedEvents = findViewById(R.id.btnSelectedEvents);

        Bundle args = new Bundle();
        args.putString("type", "entrant-waiting");
        loadFragment(new EventsFragment(), args);

        btnMyWaitingEvents.setOnClickListener(v -> {
            Bundle nArgs = new Bundle();
            nArgs.putString("type", "entrant-waiting");
            loadFragment(new EventsFragment(), nArgs);
        });

        btnMyEvents.setOnClickListener(v -> {
            Bundle nArgs = new Bundle();
            nArgs.putString("type", "eentrant-registrant");
            loadFragment(new EventsFragment(), nArgs);
        });

        btnCancelledEvents.setOnClickListener(v -> {
            Bundle nArgs = new Bundle();
            nArgs.putString("type", "entrant-cancelled");
            loadFragment(new EventsFragment(), nArgs);
        });

        btnSelectedEvents.setOnClickListener(v -> {
            Bundle nArgs = new Bundle();
            nArgs.putString("type", "entrant-selected");
            loadFragment(new EventsFragment(), nArgs);
        });

        scanQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantViewActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        // Replace the fragment in the fragmentContainer
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}
