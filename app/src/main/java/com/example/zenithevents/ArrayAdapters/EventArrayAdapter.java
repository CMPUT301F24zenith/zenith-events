package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_content, parent, false);
        }

        // Lookup view for data population
        TextView eventIdOrName = convertView.findViewById(R.id.eventIdOrName);
        TextView eventImageUrl = convertView.findViewById(R.id.eventImageUrl);
        TextView eventQRCodeURL = convertView.findViewById(R.id.eventQRCodeURL);

        // Populate the data into the template view using the data object
        eventIdOrName.setText(event.getEventId());
        eventImageUrl.setText(event.getEventImage());
        eventQRCodeURL.setText(event.getQRCodeURL());

        // Return the completed view to render on screen
        return convertView;
    }
}
