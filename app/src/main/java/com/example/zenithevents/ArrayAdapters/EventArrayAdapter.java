package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

import java.util.List;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_content, parent, false);
        }

        ImageView eventImage = convertView.findViewById(R.id.eventImage);
        TextView eventTitle = convertView.findViewById(R.id.eventTitle);
        TextView facilityName = convertView.findViewById(R.id.facilityName);

        assert event != null;
        eventTitle.setText(event.getEventTitle());
        facilityName.setText(event.getOwnerFacility());
        String imgUrl = event.getImageUrl();

        if (imgUrl != null) {
            Bitmap imgBitMap = QRCodeUtils.decodeBase64ToBitmap(imgUrl);
            Glide.with(this.getContext()).load(imgBitMap).into(eventImage);
        } else {
            eventImage.setImageResource(R.drawable.event_place_holder);
        }
        return convertView;
    }
}
