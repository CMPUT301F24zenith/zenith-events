//package com.example.zenithevents.Events;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.zenithevents.ArrayAdapters.NotificationArrayAdapter;
//import com.example.zenithevents.HelperClasses.NotificationUtils;
//import com.example.zenithevents.Objects.Notification;
//import com.example.zenithevents.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotificationActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notifications);
//
//        String deviceId = getIntent().getStringExtra("deviceId");
//
//
//        ListView notificationListView = findViewById(R.id.notificationListView);
//
//        // Sample notification data
//        List<Notification> notifications = new ArrayList<>();
//        NotificationUtils notificationUtils = new NotificationUtils();
//        notificationUtils.fetchUserNotification(deviceId, callback -> {
//            int size = callback.getEventIds().size();
//            for (int i = 0; i < size; i++) {
//                notifications.add(new Notification(deviceId, "101", "Event Update", "The event has been rescheduled."));
//            }
//
//        });
//
//        notifications.add(new Notification(deviceId, "101", "Event Update", "The event has been rescheduled."));
//        notifications.add(new Notification(deviceId, "102", "Reminder", "Don't forget to join the meeting."));
//        notifications.add(new Notification(deviceId, "103", "Thank You", "Thanks for attending the event."));
//
//        NotificationArrayAdapter adapter = new NotificationArrayAdapter(this, notifications);
//        notificationListView.setAdapter(adapter);
//    }
//}

package com.example.zenithevents.Events;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.ArrayAdapters.NotificationArrayAdapter;
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

        String deviceId = getIntent().getStringExtra("deviceId");

        ListView notificationListView = findViewById(R.id.notificationListView);

        // Initialize the notification list
        List<Notification> notifications = new ArrayList<>();

        NotificationUtils notificationUtils = new NotificationUtils();

        // Fetch the notifications asynchronously
        notificationUtils.fetchUserNotification(deviceId, callback -> {
            // This callback is executed after data is fetched
            int size = callback.getEventIds().size();

            // Add notifications to the list based on the fetched data
            for (int i = 0; i < size; i++) {
                notifications.add(new Notification(deviceId, callback.getEventIds().get(i),
                        "Event Update", "The event has been rescheduled."));
            }

            // Add additional notifications if necessary
            notifications.add(new Notification(deviceId, "101", "Event Update", "The event has been rescheduled."));
            notifications.add(new Notification(deviceId, "102", "Reminder", "Don't forget to join the meeting."));
            notifications.add(new Notification(deviceId, "103", "Thank You", "Thanks for attending the event."));

            // Now that the data is updated, create the adapter and set it to the ListView
            NotificationArrayAdapter adapter = new NotificationArrayAdapter(NotificationActivity.this, notifications);
            notificationListView.setAdapter(adapter);
        });
    }
}
