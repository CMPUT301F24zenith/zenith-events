package com.example.zenithevents.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;
import com.example.zenithevents.HelperClasses.FirestoreUserCollection;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersFragment extends Fragment {
    private static final String TAG = "ViewUsersAdminFragment";
    private ListView listView;
    private EntrantArrayAdapter adapter;
    private List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
<<<<<<<< HEAD:app/src/main/java/com/example/zenithevents/Admin/AdminUsersFragment.java
========
        listView = findViewById(R.id.listView);
        adapter = new EntrantArrayAdapter(this, userList, "admin", null);
        listView.setAdapter(adapter);
>>>>>>>> origin:app/src/main/java/com/example/zenithevents/Admin/ViewUsersAdmin.java

        // Initialize the ListView and adapter
        listView = view.findViewById(R.id.listView);
        adapter = new EntrantArrayAdapter(requireContext(), userList, "ViewUsersAdminFragment", null);
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
    public void onDestroyView() {
        super.onDestroyView();
        // Stop listening to user changes
        FirestoreUserCollection.stopListeningForUserChanges();
    }
}
