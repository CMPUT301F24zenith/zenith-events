package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.Entrant;
import com.example.zenithevents.Objects.Organization;
import com.example.zenithevents.R;

import java.util.List;

public class OrganizationArrayAdapter extends ArrayAdapter<Organization> {
    public OrganizationArrayAdapter(Context context, List<Organization> organizations) {
        super(context, 0, organizations);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Organization organiser = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.organization_content, parent, false);
        }

        // Lookup view for data population
        TextView organizationName = convertView.findViewById(R.id.organizationName);



        // Populate the data into the template view using the data object
        organizationName.setText(organiser.getOrganizationName());


        // Return the completed view to render on screen
        return convertView;
    }
}
