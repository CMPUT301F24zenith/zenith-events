package com.example.zenithevents.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;
import com.example.zenithevents.HelperClasses.FirestoreUserCollection;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersAdmin extends AppCompatActivity {
    private static final String TAG = "ViewUsersAdmin";
    ListView listView;
    private EntrantArrayAdapter adapter;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listView);
        adapter = new EntrantArrayAdapter(this, userList);
        listView.setAdapter(adapter);


        // Fetch and display user data
        FirestoreUserCollection.listenForUserChanges(users -> {
            if (users != null) {
                userList.clear();
                userList.addAll(users);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch users");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirestoreUserCollection.stopListeningForUserChanges();
    }
}

