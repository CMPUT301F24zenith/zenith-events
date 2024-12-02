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

/**
 * AdminUsersFragment is a fragment that displays and manages a list of users for the admin interface.
 * The fragment listens for real-time updates from a Firestore collection and dynamically updates the UI,
 * excluding the current user's device ID from the list.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class AdminUsersFragment extends Fragment {
    private static final String TAG = "ViewUsersAdminFragment";
    private ListView listView;
    private EntrantArrayAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private String currentUserId;

    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that this fragment's UI should be attached to, if applicable.
     * @param savedInstanceState A Bundle object containing the fragment's previously saved state, if any.
     * @return The root view of the fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView}. Initializes UI components, retrieves the current user's ID,
     * and sets up event listeners for real-time user updates from Firestore.
     *
     * @param view The View returned by {@link #onCreateView}.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state, if any.
     */
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

    /**
     * Called when the fragment's view is being destroyed. Cleans up resources and stops
     * listening for real-time user updates.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirestoreUserCollection.stopListeningForUserChanges();
    }
}
