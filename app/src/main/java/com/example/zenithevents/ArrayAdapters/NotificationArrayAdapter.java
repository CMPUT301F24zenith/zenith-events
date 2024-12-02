package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Events.EventView;
import com.example.zenithevents.Objects.Notification;
import com.example.zenithevents.R;

import java.util.List;

/**
 * Custom ArrayAdapter to display a list of {@link Notification} objects in a ListView.
 * Each notification includes event details, a message, and a subject, displayed in a card view.
 */
public class NotificationArrayAdapter extends ArrayAdapter<Notification> {
    private List<String> eventNames, eventIds, messages, subjects;

    /**
     * Constructor to initialize the adapter with data for notifications.
     *
     * @param context   The context in which the adapter is running.
     * @param eventNames A list of event names to display.
     * @param eventIds A list of event IDs for navigation.
     * @param messages A list of notification messages.
     * @param subjects A list of subjects related to the notifications.
     */
    public NotificationArrayAdapter(Context context, List<String> eventNames, List<String> eventIds, List<String> messages, List<String> subjects) {
        super(context, 0);
        this.eventNames = eventNames;
        this.eventIds = eventIds;
        this.messages = messages;
        this.subjects = subjects;
    }

    /**
     * Retrieves the view for an item at a specific position in the list.
     * This method populates the views with event details, message, and subject, and sets up a click listener
     * to navigate to the event's details when the card is clicked.
     *
     * @param position The position of the item within the list.
     * @param convertView The old view to reuse if possible.
     * @param parent The parent view that this view will be attached to.
     * @return The view for the item at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current event data from the lists using the position index
        String eventName = eventNames.get(position);
        String eventId = eventIds.get(position);
        String message = messages.get(position);
        String subject = subjects.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_content, parent, false);
        }

        // Lookup views for data population
        TextView notificationEvent = convertView.findViewById(R.id.notificationEvent);
        TextView notificationSubject = convertView.findViewById(R.id.notificationSubject);
        TextView notificationMessage = convertView.findViewById(R.id.notificationMessage);
        androidx.cardview.widget.CardView viewCard = convertView.findViewById(R.id.viewNotificationCard);

        // Populate the views with the corresponding data using the position index
        notificationEvent.setText(eventName);
        notificationSubject.setText(subject);
        notificationMessage.setText(message);

        // Set up the card view click listener to open the notification details
        viewCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EventView.class);
            intent.putExtra("eventId", eventId);
            intent.putExtra("type", "notifications");
            getContext().startActivity(intent);
        });

        // Return the completed view to render on the screen
        return convertView;
    }
}