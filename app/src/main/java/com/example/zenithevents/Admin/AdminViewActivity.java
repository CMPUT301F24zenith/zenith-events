package com.example.zenithevents.Admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.zenithevents.R;


/**
 * TODO project part 4
 * xml files in progress - not used in the app
 */
public class AdminViewActivity extends AppCompatActivity {
    private static final String TAG = "AdminViewActivity";
    private Button btnViewProfiles, btnViewEvents, btnViewFacilities;
    FrameLayout fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Log.d(TAG, "Insets: " + systemBars.toString());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnViewProfiles = findViewById(R.id.btnUsers);
        btnViewEvents = findViewById(R.id.btnEvents);
        btnViewFacilities = findViewById(R.id.btnFacilites);
        fragmentContainer = findViewById(R.id.fragment_container);

        if (savedInstanceState == null) {
            loadFragment(new AdminUsersFragment(), "users");
            updateButtonStates(btnViewProfiles);
        }
        // Set up button click listeners
        btnViewProfiles.setOnClickListener(v -> {
            loadFragment(new AdminUsersFragment(), "users");
            updateButtonStates(btnViewProfiles);
        });

        btnViewEvents.setOnClickListener(v -> {
            loadFragment(new AdminEventsFragment(), "events");
            updateButtonStates(btnViewEvents);
        });

        btnViewFacilities.setOnClickListener(v -> {
            loadFragment(new AdminFacilitiesFragment(), "facilities");
            updateButtonStates(btnViewFacilities);
        });

        }

    private void loadFragment(Fragment fragment, String tag) {
        // Check if the fragment is already displayed
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getTag() != null && currentFragment.getTag().equals(tag)) {
            Log.d(TAG, "Fragment " + tag + " is already displayed.");
            return; // Skip reloading the fragment
        }

        // Replace the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }


    private void updateButtonStates(Button activeButton) {

        resetButtonStates();

        btnViewProfiles.setTextColor(ContextCompat.getColor(this, R.color.active_button_color));
        activeButton.setCompoundDrawableTintList(ContextCompat.getColorStateList(this, R.color.active_button_color));
    }

    private void resetButtonStates() {
        // Set inactive text color for all buttons
        btnViewProfiles.setTextColor(ContextCompat.getColor(this, R.color.inactive_button_color));
        btnViewEvents.setTextColor(ContextCompat.getColor(this, R.color.inactive_button_color));
        btnViewFacilities.setTextColor(ContextCompat.getColor(this, R.color.inactive_button_color));

        btnViewProfiles.setCompoundDrawableTintList(ContextCompat.getColorStateList(this, R.color.inactive_button_color));
        btnViewEvents.setCompoundDrawableTintList(ContextCompat.getColorStateList(this, R.color.inactive_button_color));
        btnViewFacilities.setCompoundDrawableTintList(ContextCompat.getColorStateList(this, R.color.inactive_button_color));
    }


}
