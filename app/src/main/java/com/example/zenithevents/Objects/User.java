package com.example.zenithevents.Objects;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.zenithevents.EntrantDashboard.EntrantViewActivity;
import com.example.zenithevents.Manifest;
import com.example.zenithevents.R;

import java.util.ArrayList;

/**
 * Represents a user of the Zenith Events application, encapsulating details
 * such as personal information, notifications preference, role (admin or not),
 * facility association, and various event lists the user is involved with.
 *
 * <p>This class provides getters and setters for each field and supports
 * optional notifications functionality. It also includes several event-related lists
 * such as waiting, selected, registered, and cancelled events.</p>
 *
 * <p><strong>AI-Generated Documentation:</strong> The Javadocs for this class were generated
 * with assistance from a generative AI language model. The initial documentation drafts were based on
 * the code structure, including method descriptions, parameter details, and functionality explanations.
 * These drafts were then reviewed and refined to ensure clarity and accuracy, with adjustments
 * made for context and readability.</p>
 *
 * <p>This AI-generated documentation aims to enhance code readability, reduce development time, and provide
 * a clear understanding of each method’s purpose and usage within the class.</p>
 */
public class User {

    private String deviceID, firstName, lastName, email;
    private String phoneNumber;
    private String profileImageURL;
    private Boolean wantsNotifs;
    private Boolean isAdmin;
    private String myFacility;
    private ArrayList<String> waitingEvents;
    private ArrayList<String> selectedEvents;
    private ArrayList<String> registeredEvents;
    private ArrayList<String> cancelledEvents;
    private String anonymousAuthID ;

    /**
     * No-argument constructor required for Firestore.
     */
    public User() {}

    /**
     * Constructs a User object with basic user information.
     *
     * @param deviceID   The unique device ID associated with the user.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param email      The email address of the user.
     * @param phoneNumber The phone number of the user.
     */
    public User(String deviceID, String firstName, String lastName, String email, String phoneNumber) {
        this.deviceID = deviceID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.wantsNotifs = true;
        this.isAdmin = false;
        this.registeredEvents = new ArrayList<>();
        this.cancelledEvents = new ArrayList<>();
        this.selectedEvents = new ArrayList<>();
        this.waitingEvents = new ArrayList<>();

    }

    /**
     * Gets the unique device ID associated with the user.
     *
     * @return A string representing the user's device ID.
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * Gets the first name of the user.
     *
     * @return A string representing the user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return A string representing the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return A string representing the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return A string representing the user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the profile image URL of the user.
     *
     * @return A string representing the URL of the user's profile image.
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * Checks if the user wants notifications.
     *
     * @return A boolean indicating if the user wants to receive notifications.
     */
    public Boolean getWantsNotifs() {
        return wantsNotifs;
    }

    /**
     * Checks if the user has admin privileges.
     *
     * @return A boolean indicating if the user is an admin.
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Gets the facility associated with the user.
     *
     * @return A string representing the user's facility.
     */
    public String getMyFacility() {
        return myFacility;
    }

    /**
     * Sets the unique device ID for the user.
     *
     * @param deviceID A string representing the user's device ID.
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName A string representing the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName A string representing the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email A string representing the user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber A string representing the user's phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the profile image URL of the user.
     *
     * @param profileImageURL A string representing the URL of the user's profile image.
     */
    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    /**
     * Sets whether the user wants to receive notifications.
     *
     * @param wantsNotifs A boolean representing the user's notification preference.
     */
    public void setWantsNotifs(Boolean wantsNotifs) {
        this.wantsNotifs = wantsNotifs;
    }

    /**
     * Sets the admin status of the user.
     *
     * @param admin A boolean representing if the user is an admin.
     */
    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    /**
     * Sets the facility associated with the user.
     *
     * @param myFacility A string representing the user's facility.
     */
    public void setMyFacility(String myFacility) {
        this.myFacility = myFacility;
    }

    /**
     * Sends a notification message to the user.
     *
     * @param message The content of the notification message.
     */
    public void sendNotification(Context context, String message) {
        Log.d("FunctionCall", "MESSAGE: " + message + "info updated.");

        if (context == null) {
            Log.e("NotificationError", "Context is null. Cannot send notification.");
            return;
        }
        String channelID = "ZENITH_EVENTS_NOTIFICATION_CHANNEL";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Log.e("NotificationError", "NotificationManager is null.");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "Zenith Events Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for Zenith Events app");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.event_place_holder)
                .setContentTitle("Zenith Events Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent intent = new Intent(context, EntrantViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("type", "entrant-selected");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());
    }

    /**
     * Gets the list of events the user has cancelled the invitation for.
     *
     * @return An ArrayList of strings representing the cancelled events.
     */
    public ArrayList<String> getCancelledEvents() {
        return cancelledEvents;
    }

    /**
     * Gets the list of events the user is registered for.
     *
     * @return An ArrayList of strings representing the registered events.
     */
    public ArrayList<String> getRegisteredEvents() {
        return registeredEvents;
    }

    /**
     * Gets the list of events the user was selected for.
     *
     * @return An ArrayList of strings representing the selected events.
     */
    public ArrayList<String> getSelectedEvents() {
        return selectedEvents;
    }

    /**
     * Gets the list of events the user is waiting for.
     *
     * @return An ArrayList of strings representing the waiting events.
     */
    public ArrayList<String> getWaitingEvents() {
        return waitingEvents;
    }

    /**
     * Sets the list of events the user is waiting for.
     *
     * @param waitingEvents An ArrayList of strings representing the waiting events.
     */
    public void setWaitingEvents(ArrayList<String> waitingEvents) {
        this.waitingEvents = waitingEvents;
    }

    /**
     * Sets the list of events the user was selected for.
     *
     * @param selectedEvents An ArrayList of strings representing the selected events.
     */
    public void setSelectedEvents(ArrayList<String> selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    /**
     * Sets the list of events the user is registered for.
     *
     * @param registeredEvents An ArrayList of strings representing the registered events.
     */
    public void setRegisteredEvents(ArrayList<String> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

    /**
     * Sets the list of events the user has cancelled.
     *
     * @param cancelledEvents An ArrayList of strings representing the cancelled events.
     */
    public void setCancelledEvents(ArrayList<String> cancelledEvents) {
        this.cancelledEvents = cancelledEvents;
    }

    /**
     * Gets the anonymous authentication ID associated with the user.
     *
     * @return A string representing the anonymous authentication ID.
     */
    public String getAnonymousAuthID() {
        return anonymousAuthID;
    }

    /**
     * Sets the anonymous authentication ID for the user.
     *
     * @param anonymousAuthID A string representing the anonymous authentication ID.
     */
    public void setAnonymousAuthID(String anonymousAuthID) {
        this.anonymousAuthID = anonymousAuthID;
    }

}
