package com.example.zenithevents.EntrantDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;


import com.example.zenithevents.CreateProfile.CreateProfileActivity;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.MainActivity;
import com.example.zenithevents.QRCodes.QRScannerActivity;
import com.example.zenithevents.R;
import com.example.zenithevents.User.UserProfile;

import java.util.List;

public class EntrantViewActivity extends AppCompatActivity {
    private static final String TAG = "EntrantViewActivity";
    Button scanQRButton, viewProfileButton;
    private TextView currentSelection, next, previous;
    private final String[] options = {"Waitlisted Events", "Registered Events", "Events Invited To", "Cancelled Events"};
    private int currentIndex = 0;
    private UserUtils userUtils;

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

        currentSelection = findViewById(R.id.CurrentSelection);
        scanQRButton = findViewById(R.id.scanQRButton);
        next = findViewById(R.id.tvNext);
        previous = findViewById(R.id.tvPrevious);

        currentSelection.setText(options[currentIndex]);
        viewProfileButton = findViewById(R.id.viewProfileButton);

        Bundle args = new Bundle();
        args.putString("type", "entrant-waiting");
        loadFragment(new EventsFragment(), args);

        previous.setOnClickListener(v -> moveToPrevious());
        next.setOnClickListener(v -> moveToNext());

        scanQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantViewActivity.this, QRScannerActivity.class);
            startActivity(intent);
        });
        userUtils = new UserUtils();
        String currentUser = DeviceUtils.getDeviceID(this);
        userUtils.fetchUserProfile(currentUser, v -> {
            if (v == null) {
                viewProfileButton.setOnClickListener(v1 -> {
                    Intent intent = new Intent(EntrantViewActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                });
            } else {
                viewProfileButton.setOnClickListener(v1 -> {
                    Intent intent = new Intent(EntrantViewActivity.this, UserProfile.class);
                    startActivity(intent);
                });
            }
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

    private void moveToPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = options.length - 1;
        }
        updateCurrentSelection();
        loadFragmentBasedOnSelection();
    }

    private void moveToNext() {
        if (currentIndex < options.length - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        updateCurrentSelection();
        loadFragmentBasedOnSelection();
    }

    private void updateCurrentSelection() {
        currentSelection.setText(options[currentIndex]);
    }

    private void loadFragmentBasedOnSelection() {
        Fragment selectedFragment;
        Bundle nArgs = new Bundle();

        switch (currentIndex) {
            case 0:
                nArgs.putString("type", "entrant-waiting");
                break;
            case 1:
                nArgs.putString("type", "entrant-registrant");
                break;
            case 2:
                nArgs.putString("type", "entrant-selected");
                break;
            case 3:
                nArgs.putString("type", "entrant-cancelled");
                break;
            default:
                nArgs.putString("type", "entrant-waiting");
                break;
        }

        loadFragment(new EventsFragment(), nArgs);
    }
}
