package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
        ImageView eventImage = convertView.findViewById(R.id.eventImage);
        TextView eventIdOrName = convertView.findViewById(R.id.eventTitle);
        TextView facilityName = convertView.findViewById(R.id.facilityName);


        // Populate the data into the template view using the data object
        assert event != null;
        eventIdOrName.setText(event.getEventTitle());
        facilityName.setText(event.getOwnerFacility());
        // Load image using Glide
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Glide.with(getContext())
                    .load(event.getImageUrl()) // Load the image URL from the Event object
                    .apply(new RequestOptions()
                            .transform(new RoundedCorners(16)) // Apply rounded corners transformation
                            .placeholder(R.drawable.event_place_holder) // Optional placeholder while loading

                    )
                    .into(eventImage); // Load into the ImageView
        } else {
            // Set a default image if no URL is provided
            eventImage.setImageResource(R.drawable.event_place_holder);



        }
        return convertView;
    }
}
