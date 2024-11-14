package com.example.zenithevents.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.zenithevents.R;


/**
 * TODO project part 4
 * xml files in progress - not used in the app
 */
public class AdminViewActivity extends AppCompatActivity {
    private static final String TAG = "AdminViewActivity";
    private Button btnViewProfiles, btnViewEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnViewProfiles = findViewById(R.id.btnViewProfiles);
        btnViewEvents = findViewById(R.id.btnViewEvents);


        btnViewProfiles.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewActivity.this, ViewUsersAdmin.class);
            startActivity(intent);
        });
        }
    }
