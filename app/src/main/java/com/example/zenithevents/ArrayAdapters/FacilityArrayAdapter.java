package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;

import java.util.List;

public class FacilityArrayAdapter extends ArrayAdapter<Facility> {
    public FacilityArrayAdapter(Context context, List<Facility> facilities){
        super(context, 0, facilities);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Facility facility = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_content, parent, false);
        }

        // Lookup view for data population
        TextView facilityName = convertView.findViewById(R.id.facilityName);
        TextView facilityAddress = convertView.findViewById(R.id.facilityAddress);

        // Populate the data into the template view using the data object
        facilityName.setText(facility.getFacilityName());
        facilityAddress.setText(facility.getAddress());

        // Return the completed view to render on screen
        return convertView;
    }
}
