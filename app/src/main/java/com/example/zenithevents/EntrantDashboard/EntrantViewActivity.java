package com.example.zenithevents.EntrantDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;


import com.example.zenithevents.EntrantsList.EventsFragment;
import com.example.zenithevents.Events.NotificationActivity;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.FirestoreEventsCollection;
import com.example.zenithevents.HelperClasses.FirestoreUserCollection;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Events.QRScannerActivity;
import com.example.zenithevents.R;
import com.example.zenithevents.User.CreateProfileActivity;
import com.example.zenithevents.User.UserProfile;

/**
 * Activity that displays the entrant dashboard, allowing the user to navigate through various
 * event lists, scan QR codes, and view or create a profile.
 * <p>Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.</p>
 */
public class EntrantViewActivity extends AppCompatActivity {
    private static final String TAG = "EntrantViewActivity";
    ImageButton scanQRButton, viewProfileButton, sendNotificationButton;
    private TextView currentSelection, next, previous;
    private final String[] options = {"Events Invited To", "Registered Events", "Waitlisted Events", "Cancelled Events"};
    private int currentIndex = 0;
    private UserUtils userUtils;

    /**
     * Initializes the EntrantViewActivity, sets up views, and handles user interactions such as
     * navigating through event categories and scanning QR codes.
     * <p>Handles the setup of the view layout, and assigns click listeners to buttons for
     * navigating through the event categories and scanning QR codes. It also checks if the user has a
     * profile and allows the user to create or view their profile accordingly.</p>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in {@link #onSaveInstanceState}. Otherwise, it is {@code null}.
     */
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
        sendNotificationButton = findViewById(R.id.notificationButton);

        Bundle args = new Bundle();
        args.putString("type", "entrant-selected");
        loadFragment(new EventsFragment(), args);

        previous.setOnClickListener(v -> moveToPrevious());
        next.setOnClickListener(v -> moveToNext());

        scanQRButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRScannerActivity.class);
            startActivity(intent);
        });
        userUtils = new UserUtils();
        String currentUser = DeviceUtils.getDeviceID(this);
        sendNotificationButton.setOnClickListener(v -> {
            Log.d("FunctionCall", "CLICKED NOTIF");
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

        FirestoreUserCollection.listenForSpecificUserChanges(currentUser, user -> {
            if (user == null) {
                viewProfileButton.setOnClickListener(v1 -> {
                    Intent intent = new Intent(this, CreateProfileActivity.class);
                    startActivity(intent);
                });
            } else {
                viewProfileButton.setOnClickListener(v1 -> {
                    Intent intent = new Intent(this, UserProfile.class);
                    startActivity(intent);
                });
            }
        });
    }

    /**
     * Loads a fragment into the fragment container.
     *
     * @param fragment The fragment to be loaded.
     * @param args The arguments to pass to the fragment.
     */
    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);

        // Replace the fragment in the fragmentContainer
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Moves to the previous event category in the selection options.
     * Updates the current selection and loads the appropriate fragment based on the new selection.
     */
    private void moveToPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = options.length - 1;
        }
        updateCurrentSelection();
        loadFragmentBasedOnSelection();
    }

    /**
     * Moves to the next event category in the selection options.
     * Updates the current selection and loads the appropriate fragment based on the new selection.
     */
    private void moveToNext() {
        if (currentIndex < options.length - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        updateCurrentSelection();
        loadFragmentBasedOnSelection();
    }

    /**
     * Updates the displayed text to show the current event category.
     */
    private void updateCurrentSelection() {
        currentSelection.setText(options[currentIndex]);
    }

    /**
     * Loads the appropriate fragment based on the selected event category.
     */
    private void loadFragmentBasedOnSelection() {
        Bundle nArgs = new Bundle();

        switch (options[currentIndex]) {
            case "Registered Events":
                nArgs.putString("type", "entrant-registrant");
                break;
            case "Events Invited To":
                nArgs.putString("type", "entrant-selected");
                break;
            case "Cancelled Events":
                nArgs.putString("type", "entrant-cancelled");
                break;
            default:
                nArgs.putString("type", "entrant-waiting");
                break;
        }

        loadFragment(new EventsFragment(), nArgs);
    }
}
