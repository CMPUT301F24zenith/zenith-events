package com.example.zenithevents.ArrayAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.Organizer.EventView;
import com.example.zenithevents.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

/**
 * Custom ArrayAdapter to display a list of {@link Event} objects in a list view.
 * This adapter is responsible for populating each item in the list with relevant event data
 * and providing a click event to navigate to the EventView activity.
 *
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {

    private FirebaseFirestore db;
    FacilityUtils facilityUtils;
    String deviceId, type;
    List<Event> events;

    /**
     * Constructor for the EventArrayAdapter.
     *
     * <p>This constructor initializes the adapter with the context and the list of events to be displayed.
     * It calls the super constructor with a resource value of 0, as no specific layout resource is required
     * for this adapter.</p>
     *
     * @param context The current context in which the adapter is running.
     * @param events The list of events to be displayed.
     */
    public EventArrayAdapter(Context context, List<Event> events, String type, String deviceId) {
        super(context, 0, events);
        this.events = events;
        this.type = type;
        this.deviceId = deviceId;
    }

    /**
     * Retrieves the view for a particular list item, inflating the view layout and populating it with data
     * for the corresponding {@link Event}.
     *
     * <p>This method checks if a view is available for reuse; if not, it inflates a new one. Then, it populates
     * the views with the data from the {@link Event} object at the specified position.</p>
     *
     * @param position The position of the item within the adapter’s data set to be displayed.
     * @param convertView A recycled view that can be reused (or {@code null} if not available).
     * @param parent The parent view that this view will eventually be attached to.
     *
     * @return A {@link View} object representing the item at the given position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        facilityUtils = new FacilityUtils();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_content, parent, false);
        }

        ImageView eventImage = convertView.findViewById(R.id.eventImage);
        TextView eventTitle = convertView.findViewById(R.id.eventTitle);
        TextView facilityName = convertView.findViewById(R.id.facilityName);
        Button acceptBtn = convertView.findViewById(R.id.acceptEventBtn);
        Button declineBtn = convertView.findViewById(R.id.declineEventBtn);

        String deviceId = DeviceUtils.getDeviceID(getContext());

        UserUtils userUtils = new UserUtils();

        if (Objects.equals(this.type, "selectedEvents") ||
                Objects.equals(this.type, "waitingEvents") ||
                Objects.equals(this.type, "registeredEvents") ||
                Objects.equals(this.type, "organizer") ||
                Objects.equals(this.type, "admin")
        ) {
            declineBtn.setVisibility(View.VISIBLE);

            if (Objects.equals(this.type, "selectedEvents")) {
                acceptBtn.setVisibility(View.VISIBLE);
                acceptBtn.setOnClickListener(v -> {
                    userUtils.acceptEventInvitation(deviceId, event.getEventId());
                    events.remove(position);
                    notifyDataSetChanged();
                });
            }
            if (!Objects.equals(this.type, "organizer") && !Objects.equals(this.type, "admin")) {
                declineBtn.setOnClickListener(v -> userUtils.applyLeaveEvent(getContext(), deviceId, event.getEventId(), callback -> {
                    if (callback) {
                        Toast.makeText(getContext(), "Declined Event Invitation", Toast.LENGTH_SHORT).show();
                    }
                    events.remove(position);
                    notifyDataSetChanged();
                }));
            }
            if (Objects.equals(this.type, "organizer") || Objects.equals(this.type, "admin")) {
                // delete event
            }

        } else {
            acceptBtn.setVisibility(View.GONE);
            declineBtn.setVisibility(View.GONE);
        }

        assert event != null;
        eventTitle.setText(event.getEventTitle());
        facilityUtils.fetchFacilityName(event.getOwnerFacility(), v -> {
            if (v != null) {
                facilityName.setText(v);
            } else {
                facilityName.setText("");
            }
        });

        String imgUrl = event.getImageUrl();

        if (imgUrl != null) {
            Bitmap imgBitMap = QRCodeUtils.decodeBase64ToBitmap(imgUrl);
            Glide.with(this.getContext()).load(imgBitMap).into(eventImage);
        } else {
            eventImage.setImageResource(R.drawable.event_place_holder);
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EventView.class);
            intent.putExtra("event_id", event.getEventId());
            getContext().startActivity(intent);
        });

        return convertView;
    }

}