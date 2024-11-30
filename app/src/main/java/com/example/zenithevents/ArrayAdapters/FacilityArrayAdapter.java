package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zenithevents.Objects.Facility;
import com.example.zenithevents.R;
import com.example.zenithevents.Admin.FacilityDetail;

import java.util.List;

/**
 * Custom ArrayAdapter to display a list of {@link Facility} objects in a list view.
 * This adapter is responsible for populating each item in the list with relevant facility data.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class FacilityArrayAdapter extends ArrayAdapter<Facility> {

    /**
     * Constructor for the FacilityArrayAdapter.
     *
     * <p>This constructor initializes the adapter with the context and the list of facilities to be displayed.
     * It calls the super constructor with a resource value of 0, as no specific layout resource is required
     * for this adapter.</p>
     *
     * @param context The current context in which the adapter is running.
     * @param facilities The list of facilities to be displayed.
     */
    public FacilityArrayAdapter(Context context, List<Facility> facilities){
        super(context, 0, facilities);
    }

    /**
     * Retrieves the view for a particular list item, inflating the view layout and populating it with data
     * for the corresponding {@link Facility}.
     *
     * <p>This method checks if a view is available for reuse; if not, it inflates a new one. Then, it populates
     * the views with the data from the {@link Facility} object at the specified position.</p>
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
        Facility facility = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_content, parent, false);
        }

        // Lookup view for data population
        TextView facilityName = convertView.findViewById(R.id.facilityName);
        TextView facilityAddress = convertView.findViewById(R.id.facilityAddress);
        androidx.cardview.widget.CardView viewCard = convertView.findViewById(R.id.viewFacilityCard);

        // Populate the data into the template view using the data object
        facilityName.setText(facility.getNameOfFacility());
        facilityAddress.setText(facility.getEmailOfFacility());

        viewCard.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), FacilityDetail.class);
            intent.putExtra("facilityId", facility.getDeviceId());
            intent.putExtra("facilityName", facility.getNameOfFacility());
            intent.putExtra("facilityEmail", facility.getEmailOfFacility());
            intent.putExtra("facilityPhoneNumber", facility.getPhoneOfFacility());
            getContext().startActivity(intent);
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
