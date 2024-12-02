package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.Notification;
import com.example.zenithevents.R;

import java.util.List;

/**
 * Custom ArrayAdapter to display a list of {@link Notification} objects in a list view.
 */
public class NotificationArrayAdapter extends ArrayAdapter<Notification> {

    /**
     * Constructor for the NotificationArrayAdapter.
     *
     * @param context The current context in which the adapter is running.
     * @param notifications The list of notifications to be displayed.
     */
    public NotificationArrayAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
    }

    /**
     * Retrieves the view for a particular list item, inflating the view layout and populating it with data
     * for the corresponding {@link Notification}.
     *
     * @param position The position of the item within the adapter’s data set to be displayed.
     * @param convertView A recycled view that can be reused (or {@code null} if not available).
     * @param parent The parent view that this view will eventually be attached to.
     *
     * @return A {@link View} object representing the item at the given position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Notification notification = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_content, parent, false);
        }

        // Lookup view for data population
        TextView notificationEvent = convertView.findViewById(R.id.notificationEvent);
        TextView notificationSubject = convertView.findViewById(R.id.notificationSubject);
        TextView notificationMessage = convertView.findViewById(R.id.notificationMessage);
        androidx.cardview.widget.CardView viewCard = convertView.findViewById(R.id.viewNotificationCard);

        // Populate the data into the template view using the data object
        notificationSubject.setText(notification.getSubject());
        notificationMessage.setText(notification.getMessage());

        // Set up click listener to open details
        viewCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationDetailActivity.class);
            intent.putExtra("eventID", notification.getEventID());
            intent.putExtra("subject", notification.getSubject());
            intent.putExtra("message", notification.getMessage());
            getContext().startActivity(intent);
        });

        // Return the completed view to render on screen
        return convertView;
    }
}