package com.example.zenithevents.Events;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.ArrayAdapters.NotificationArrayAdapter;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.NotificationUtils;
import com.example.zenithevents.Objects.Notification;
import com.example.zenithevents.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Log.d("FunctionCall", "NotificationActivity onCreate");

        String deviceId = DeviceUtils.getDeviceID(this);

        ListView notificationListView = findViewById(R.id.notificationListView);

        // Lists to hold the data fetched from Firestore
        List<String> eventNames = new ArrayList<>();
        List<String> eventIds = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        List<String> subjects = new ArrayList<>();

        // Create a NotificationUtils instance to fetch notifications
        NotificationUtils notificationUtils = new NotificationUtils();

        // Fetch the notifications
        notificationUtils.fetchUserNotification(deviceId, callback -> {
            Log.d("FunctionCall", "fetchUserNotification");

            // Ensure data is fetched
            eventNames.addAll(callback.getEventNames());
            eventIds.addAll(callback.getEventIds());
            messages.addAll(callback.getMessages());
            subjects.addAll(callback.getSubjects());

            // Create an adapter and set it to the ListView once data is available
            NotificationArrayAdapter adapter = new NotificationArrayAdapter(
                    NotificationActivity.this, eventNames, eventIds, messages, subjects);
            notificationListView.setAdapter(adapter);
        });
    }
}
