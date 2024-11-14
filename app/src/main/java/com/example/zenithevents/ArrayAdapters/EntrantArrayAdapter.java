package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zenithevents.HelperClasses.InitialsGenerator;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.User;
import com.example.zenithevents.R;
import com.example.zenithevents.User.ProfileDetailActivity;

import java.util.List;

/**
 * Custom ArrayAdapter to display a list of {@link User} objects in a list view.
 * This adapter is responsible for populating each item in the list with relevant user data.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class EntrantArrayAdapter extends ArrayAdapter<User> {

    /**
     * Constructor for the EntrantArrayAdapter.
     *
     * <p>This constructor initializes the adapter with the context and the list of users to be displayed.
     * It calls the super constructor with a resource value of 0, as no specific layout resource is required
     * for this adapter.</p>
     *
     * @param context The current context in which the adapter is running.
     * @param users The list of users to be displayed.
     */
    public EntrantArrayAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    /**
     * Retrieves the view for a particular list item, inflating the view layout and populating it with data
     * for the corresponding {@link User}.
     *
     * <p>This method checks if a view is available for reuse; if not, it inflates a new one. Then, it populates
     * the views with the data from the {@link User} object at the specified position.</p>
     *
     * @param position The position of the item within the adapter’s data set to be displayed.
     * @param convertView A recycled view that can be reused (or {@code null} if not available).
     * @param parent The parent view that this view will eventually be attached to.
     *
     * @return A {@link View} object representing the item at the given position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entrant_content, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.entrantName);
        TextView emailView = convertView.findViewById(R.id.entrantEmail);
        TextView initials = convertView.findViewById(R.id.initials);
        ImageView profileImage = convertView.findViewById(R.id.profileImage);

        if (user != null) {
            String firstName = user.getFirstName() != null ? user.getFirstName() : "No FirstName";
            String lastName = user.getLastName() != null ? user.getLastName() : "No LastName";
            String email = user.getEmail() != null ? user.getEmail() : "No Email";

            nameView.setText(firstName + " " + lastName);
            emailView.setText(email);

            if (user.getProfileImageURL() != null && !user.getProfileImageURL().isEmpty()) {
                Bitmap imgBitmap = QRCodeUtils.decodeBase64ToBitmap(user.getProfileImageURL());
                profileImage.setImageBitmap(imgBitmap);
                initials.setVisibility(View.GONE);
            } else {
                profileImage.setImageResource(R.drawable.circle_background);
                initials.setText(InitialsGenerator.getInitials(firstName, lastName));
                initials.setVisibility(View.VISIBLE);
            }

            convertView.setOnClickListener(v -> {
                if (user.getDeviceID() != null) {
                    Intent intent = new Intent(getContext(), ProfileDetailActivity.class);
                    intent.putExtra("userID", user.getDeviceID());
                    getContext().startActivity(intent);
                } else {
                    Log.e("EntrantArrayAdapter", "User device ID is null");
                }
            });
        } else {
            Log.e("EntrantArrayAdapter", "User object is null at position: " + position);
        }

        return convertView;
    }
}
