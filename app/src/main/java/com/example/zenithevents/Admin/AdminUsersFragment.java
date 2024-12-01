package com.example.zenithevents.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.zenithevents.ArrayAdapters.EntrantArrayAdapter;
import com.example.zenithevents.HelperClasses.DeviceUtils;
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
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = DeviceUtils.getDeviceID(requireContext()); // we get the current user Id to exclude from the list

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = view.findViewById(R.id.listView);
        adapter = new EntrantArrayAdapter(requireContext(), userList, "admin", null);
        listView.setAdapter(adapter);



        listView = view.findViewById(R.id.listView);
        adapter = new EntrantArrayAdapter(requireContext(), userList, "admin", null);
        listView.setAdapter(adapter);
        listView.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.list_layout_animation)
        );

        FirestoreUserCollection.listenForUserChanges(users -> {
            if (users != null) {
                userList.clear();

                for (User user : users) {
                    if (!user.getDeviceID().equals(currentUserId)) {
                        userList.add(user);
                        listView.scheduleLayoutAnimation();
                    }
                }

                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "Failed to fetch users");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirestoreUserCollection.stopListeningForUserChanges();
    }


}
