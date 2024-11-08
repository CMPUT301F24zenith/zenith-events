package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;

import java.util.List;

public class EntrantArrayAdapter extends ArrayAdapter<User> {
    public EntrantArrayAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrant_content, parent, false);
        }

        // Lookup view for data population
        TextView nameView = convertView.findViewById(R.id.entrantName);
        TextView emailView = convertView.findViewById(R.id.entrantEmail);
        TextView phoneNumberView = convertView.findViewById(R.id.entrantPhoneNumber);
        TextView profileImageView = convertView.findViewById(R.id.profileImage);

        // Populate the data into the template view using the data object
        assert user != null;
        nameView.setText(user.getFirstName() + " " + user.getLastName());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhoneNumber());
        profileImageView.setText(user.getProfileImageURL());

        // Return the completed view to render on screen
        return convertView;
    }
}
