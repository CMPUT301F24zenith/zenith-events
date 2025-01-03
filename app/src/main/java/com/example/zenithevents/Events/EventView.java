package com.example.zenithevents.Events;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.zenithevents.Admin.AdminEventsFragment;
import com.example.zenithevents.Admin.AdminViewActivity;
import com.example.zenithevents.Admin.FacilityDetail;
import com.example.zenithevents.Admin.QRViewAdmin;
import com.example.zenithevents.EntrantsList.CancelledEntrants;
import com.example.zenithevents.EntrantsList.EnrolledEntrants;
import com.example.zenithevents.EntrantsList.MapActivity;
import com.example.zenithevents.EntrantsList.SampledEntrants;
import com.example.zenithevents.EntrantsList.WaitlistedEntrants;
import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.DeviceUtils;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.FacilityUtils;
import com.example.zenithevents.HelperClasses.UserUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.example.zenithevents.User.OrganizerPage;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents the EventView activity in the application, which displays
 * the details of an event, allowing users to join/leave the waiting list, view the
 * event's description and image, and perform various event-related actions.
 *
 * <p>Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class EventView extends AppCompatActivity {

    private static final String TAG = "EventView";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    ImageView eventPosterImageView;
    private Button btnJoinLeaveWaitingList, btnSampleUsers, deleteEventButton, deleteImageButton;
    private TextView eventDescription, eventName, facilityName, eventAddress;
    private ImageButton sendNotifsButton,mapButton, btnEditEvent, qrCodeButton;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration eventListener;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final String[] entrantOptions = {"Waitlisted Entrants", "Selected Entrants", "Registered Entrants", "Cancelled Entrants"};
    private int currentOptionIndex = 0;
    LinearLayout entrantsSlider;
    FacilityUtils facilityUtils = new FacilityUtils();
    EventUtils eventUtils = new EventUtils();
    private String eventId, type;
    private LottieAnimationView lotteryAnimation, joinEventAnimation, sendNotifAnimation, deleteEventAnimation, joinEventLoadingAnimation;


    /**
     * Initializes the activity and its UI components.
     *
     * @param savedInstanceState Contains data supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eventId = getIntent().getStringExtra("event_id");
        type = getIntent().getStringExtra("type");
        initializeViews();

        setupRealTimeEventListener(eventId);
    }

    /**
     * Initializes views used in the activity.
     */
    private void initializeViews() {
        eventPosterImageView = findViewById(R.id.eventImage);
        btnJoinLeaveWaitingList = findViewById(R.id.btnJoinWaitingList);
        facilityName = findViewById(R.id.facilityName);
        eventAddress = findViewById(R.id.eventAddress);
        progressBar = findViewById(R.id.progressBar);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        qrCodeButton = findViewById(R.id.qrCodeButton);
        entrantsSlider = findViewById(R.id.entrantsSlider);
        btnEditEvent = findViewById(R.id.btnEditEvent);
        deleteEventButton = findViewById(R.id.deleteEvent);
        btnSampleUsers = findViewById(R.id.btnSampleUsers);
        deleteImageButton = findViewById(R.id.deleteImage);
        mapButton = findViewById(R.id.mapButton);
        sendNotifsButton = findViewById(R.id.sendNotifsButton);
        lotteryAnimation = findViewById(R.id.lotteryAnimation);
        joinEventAnimation = findViewById(R.id.joinEventAnimation);
        sendNotifAnimation = findViewById(R.id.sendNotifAnimation);
        deleteEventAnimation = findViewById(R.id.deleteEventAnimation);
        joinEventLoadingAnimation = findViewById(R.id.joinEventLoadingAnimation);
    }

    /**
     * Sets up a real-time listener for the event document in Firestore.
     *
     * @param eventId The unique identifier of the event.
     */
    private void setupRealTimeEventListener(String eventId) {
        if (eventId == null) {
            Log.e(TAG, "Event ID not provided in the Intent.");
            return;
        }

        // Show progress bar while loading
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Set up the real-time listener for the event document
        eventListener = db.collection("events").document(eventId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Listen failed.", e);
                        progressBar.setVisibility(ProgressBar.GONE);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        this.eventId = event != null ? event.getEventId() : null;

                        if (event != null) {
                            displayEventDetails(event);
                        }
                    } else {
                        Log.e(TAG, "Event document does not exist.");
                    }

                    // Hide progress bar after loading
                    progressBar.setVisibility(ProgressBar.GONE);
                });
    }

    /**
     * Displays details of the event on the UI.
     *
     * @param event The event object containing event details.
     */
    private void displayEventDetails(Event event) {
        Log.d("FunctionCall", "displayingDetails");

        eventName.setText(event.getEventName());
        facilityUtils.fetchFacilityName(event.getOwnerFacility(), v -> {
            if (v != null) {
                facilityName.setText(v);
            } else {
                facilityName.setText("");
            }
        });

        if (event.getImageUrl() == null || event.getImageUrl().isEmpty()) {
            Log.d("ImageCheck", "No image available for this event.");
            deleteImageButton.setVisibility(View.GONE);
            eventPosterImageView.setImageResource(R.drawable.event_place_holder);
        } else {
            Log.d("ImageCheck", "Image available. Showing delete button.");
            if(Objects.equals(type, "admin")) deleteImageButton.setVisibility(View.VISIBLE);
            loadImage(event.getImageUrl(), eventPosterImageView);
        }

        eventAddress.setText(event.getEventAddress());
        if (event.getEventAddress().isEmpty()) {
            eventAddress.setVisibility(View.GONE);
        } else {
            eventAddress.setVisibility(View.VISIBLE);
        }
        eventDescription.setText(event.getEventDescription());
        UserUtils userUtils = new UserUtils();
        String deviceID = DeviceUtils.getDeviceID(this);

        Log.d("FunctionCall", "deviceID: " + deviceID);

        if (event.getCancelledList().contains(deviceID) ||
                event.getRegistrants().contains(deviceID)) {
            btnJoinLeaveWaitingList.setEnabled(false);

            btnJoinLeaveWaitingList.setText("Attendance can not be changed");
            btnJoinLeaveWaitingList.setBackgroundColor(getResources().getColor(R.color.inactive_button_color));
        }

        if (event.getSelected().contains(deviceID)) {
            btnJoinLeaveWaitingList.setVisibility(View.GONE);
        }

        if (event.getWaitingList().contains(deviceID)) {
            btnJoinLeaveWaitingList.setBackgroundColor(getResources().getColor(R.color.negative_button_color));
            btnJoinLeaveWaitingList.setTextColor(Color.WHITE);
            btnJoinLeaveWaitingList.setText("Leave Event");
            btnJoinLeaveWaitingList.setEnabled(true);

            btnJoinLeaveWaitingList.setOnClickListener(v -> {
                joinEventLoadingAnimation.setVisibility(View.VISIBLE);
                joinEventLoadingAnimation.setSpeed(0.5f);
                joinEventLoadingAnimation.playAnimation();

                btnSampleUsers.setEnabled(false);
                btnJoinLeaveWaitingList.setEnabled(false);
                deleteEventButton.setEnabled(false);

                userUtils.applyLeaveEvent(this, deviceID, event.getEventId(), (isSuccess, event_) -> {
                    if (isSuccess == 0) {
                        joinEventLoadingAnimation.setVisibility(View.GONE);
                        btnSampleUsers.setEnabled(true);
                        btnJoinLeaveWaitingList.setEnabled(true);
                        deleteEventButton.setEnabled(true);
                        Toast.makeText(this, "Successfully left the event!", Toast.LENGTH_SHORT).show();
                    } else if (isSuccess == -1) {
                        joinEventLoadingAnimation.setVisibility(View.GONE);
                        btnSampleUsers.setEnabled(true);
                        btnJoinLeaveWaitingList.setEnabled(true);
                        deleteEventButton.setEnabled(true);
                        Toast.makeText(this, "Failed to change attendance. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        if (
                !event.getWaitingList().contains(deviceID) &&
                !event.getCancelledList().contains(deviceID) &&
                !event.getSelected().contains(deviceID) &&
                !event.getRegistrants().contains(deviceID)
        ) {
            btnJoinLeaveWaitingList.setBackgroundColor(getResources().getColor(R.color.positive_button_color));
            btnJoinLeaveWaitingList.setTextColor(Color.WHITE);
            btnJoinLeaveWaitingList.setText("Join Waiting List");
            btnJoinLeaveWaitingList.setEnabled(true);

            btnJoinLeaveWaitingList.setOnClickListener(v -> {
                joinEventLoadingAnimation.setVisibility(View.VISIBLE);
                joinEventLoadingAnimation.setSpeed(0.5f);
                joinEventLoadingAnimation.playAnimation();

                btnSampleUsers.setEnabled(false);
                btnJoinLeaveWaitingList.setEnabled(false);
                deleteEventButton.setEnabled(false);

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);

                    joinEventLoadingAnimation.setVisibility(View.GONE);
                    btnSampleUsers.setEnabled(true);
                    btnJoinLeaveWaitingList.setEnabled(true);
                    deleteEventButton.setEnabled(true);
                }

                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        userUtils.applyLeaveEvent(this, deviceID, event.getEventId(), (isSuccess, event_) -> {
                            Log.d("FunctionCall", "Applying.." + isSuccess);
                            if (isSuccess == 1) {
                                if (event_.getHasGeolocation()) {
                                    Log.d("FunctionCall", "location::" + location.toString());

                                    if (location != null) {
                                        double latitude = location.getLatitude();
                                        Log.d("FunctionCall", "EVENTID" + event_.getEventId());

                                        Log.d("FunctionCall", "Latitude" + latitude);
                                        double longitude = location.getLongitude();
                                        Log.d("FunctionCall", "Longitude:" + longitude);
                                        event_.updateUserLocation(deviceID, latitude, longitude);

                                        Log.d("FunctionCall", "updatingEvent...");
                                        eventUtils.createUpdateEvent(event_, callback -> {
                                            joinEventLoadingAnimation.setVisibility(View.GONE);

                                            if (callback != null) {
                                                joinEventAnimation.setVisibility(View.VISIBLE);
                                                joinEventAnimation.setSpeed(0.5f);
                                                joinEventAnimation.playAnimation();

                                                btnSampleUsers.setEnabled(true);
                                                btnJoinLeaveWaitingList.setEnabled(true);
                                                deleteEventButton.setEnabled(true);
                                                Log.d("FunctionCall", "Location added successfully.");
                                            } else {
                                                Log.d("FunctionCall", "Failed to update event.");
                                            }
                                        });
                                    } else {
                                        Log.d("Location", "Location is null");
                                    }
                                } else {
                                    joinEventLoadingAnimation.setVisibility(View.GONE);

                                    joinEventAnimation.setVisibility(View.VISIBLE);
                                    joinEventAnimation.setSpeed(0.5f);
                                    joinEventAnimation.playAnimation();
                                }
                                Log.d("EventView", "Successfully joined the event!");
                            } else if (isSuccess == 0) {
                                joinEventLoadingAnimation.setVisibility(View.GONE);

                                btnSampleUsers.setEnabled(true);
                                btnJoinLeaveWaitingList.setEnabled(true);
                                deleteEventButton.setEnabled(true);
                                Toast.makeText(this, "Successfully left the event!", Toast.LENGTH_SHORT).show();
                            } else if (isSuccess == -1) {
                                joinEventLoadingAnimation.setVisibility(View.GONE);

                                btnSampleUsers.setEnabled(true);
                                btnJoinLeaveWaitingList.setEnabled(true);
                                deleteEventButton.setEnabled(true);
                                Toast.makeText(this, "Failed to join event. Please try again.", Toast.LENGTH_SHORT).show();
                            }

                        });
                    });

                // Handles the visibility and button states during the lifecycle of an animation.
                    joinEventAnimation.addAnimatorListener(new Animator.AnimatorListener() {
                        /**
                         * Called when the animation starts. Makes the `joinEventAnimation` view visible.
                         *
                         * @param animation The animator that started the animation.
                         */
                        public void onAnimationStart(Animator animation) {
                            joinEventAnimation.setVisibility(View.VISIBLE);
                        }

                        /**
                         * Called when the animation ends. Hides the `joinEventAnimation` view and re-enables the buttons.
                         *
                         * @param animation The animator that ended the animation.
                         */
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            joinEventAnimation.setVisibility(View.GONE);

                            btnSampleUsers.setEnabled(true);
                            btnJoinLeaveWaitingList.setEnabled(true);
                            deleteEventButton.setEnabled(true);
                        }

                        /**
                         * Called when the animation is canceled. Ensures that the `joinEventAnimation` view is hidden.
                         *
                         * @param animation The animator that was canceled.
                         */
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            joinEventAnimation.setVisibility(View.GONE);
                        }

                        /**
                         * Called when the animation repeats. No action is taken.
                         *
                         * @param animation The animator that is repeating.
                         */
                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            });

            if (event.getNumParticipants() != 0 &&
                    event.getCancelledList().size() + event.getSelected().size() + event.getRegistrants().size() + event.getWaitingList().size() >= event.getNumParticipants()) {
                btnJoinLeaveWaitingList.setEnabled(false);

                btnJoinLeaveWaitingList.setText("Event is full");
                btnJoinLeaveWaitingList.setBackgroundColor(Color.GRAY);
            }
        }

        sendNotifsButton.setOnClickListener(v -> {
            String[] options = {"Registered Entrants", "Selected Entrants", "Cancelled Entrants", "Waitlisted Entrants"};
            boolean[] selectedOptions = new boolean[options.length];
            AtomicBoolean optionSelected = new AtomicBoolean(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send Notifications To");
            builder.setMultiChoiceItems(options, selectedOptions, (dialog, which, isChecked) -> {
                selectedOptions[which] = isChecked;
            });
            builder.setPositiveButton("Next", (dialog, which) -> {
                ArrayList<String> selectedEntrants = new ArrayList<>();
                if (selectedOptions[0]) {
                    optionSelected.set(true);
                    if (!event.getRegistrants().isEmpty())
                        selectedEntrants.addAll(event.getRegistrants());
                }
                if (selectedOptions[1]) {
                    optionSelected.set(true);
                    if (!event.getSelected().isEmpty())
                        selectedEntrants.addAll(event.getSelected());
                }
                if (selectedOptions[2]) {
                    optionSelected.set(true);
                    if (!event.getCancelledList().isEmpty())
                        selectedEntrants.addAll(event.getCancelledList());
                }
                if (selectedOptions[3]){
                    optionSelected.set(true);
                    if (!event.getWaitingList().isEmpty())
                        selectedEntrants.addAll(event.getWaitingList());
                }

                if (!optionSelected.get()){
                    Toast.makeText(this, "Please select at least one option.", Toast.LENGTH_SHORT).show();
                }
                else if (!selectedEntrants.isEmpty()) {
                    showMessageInputDialog(this, selectedEntrants, event);
                }
                else if (selectedEntrants.isEmpty()) {
                    Toast.makeText(this, "There is no one is the lists you have selected", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.create().show();

        });


        loadImage(event.getImageUrl(), eventPosterImageView);
        Log.d("FunctionCAll", "type:: " + type);
        if (Objects.equals(type, "organizer") || Objects.equals(type, "admin")) {
            setupEntrantNavigation(event.getEventId());
            if (Objects.equals(type, "admin")) {

                deleteImageButton.setOnClickListener(v ->
                        new AlertDialog.Builder(this)
                                .setTitle("Remove Event Picture")
                                .setMessage("Are you sure you want to remove the event's poster picture?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    removePicture();
                                })
                                .setNegativeButton("No", null)
                                .show()
                );
            }

            if (!event.getWaitingList().isEmpty() && event.getHasGeolocation()) {
                mapButton.setVisibility(View.VISIBLE);
                mapButton.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.putExtra("EventID", event.getEventId());
                    startActivity(intent);
                });
            } else {
                mapButton.setVisibility(View.GONE);
            }

            if (Objects.equals(type, "organizer")) btnEditEvent.setVisibility(View.VISIBLE);
            if (Objects.equals(type, "organizer")) sendNotifsButton.setVisibility(View.VISIBLE);
            btnEditEvent.setOnClickListener(v -> {
                Intent intent = new Intent(this, CreateEventPage.class);
                intent.putExtra("Event Id", event.getEventId());
                startActivity(intent);
            });

            if (Objects.equals(type, "organizer")) btnSampleUsers.setVisibility(View.VISIBLE);
            btnSampleUsers.setOnClickListener(v -> {
                btnSampleUsers.setEnabled(false);
                btnJoinLeaveWaitingList.setEnabled(false);
                deleteEventButton.setEnabled(false);

                event.drawLottery(this);

                lotteryAnimation.setVisibility(View.VISIBLE);
                lotteryAnimation.playAnimation();
            });

            joinEventLoadingAnimation.removeAllAnimatorListeners();
            // Handles the visibility and button states during the lifecycle of an animation.
            joinEventLoadingAnimation.addAnimatorListener(new Animator.AnimatorListener() {

                /**
                 * This method is called when the animation starts.
                 * It makes the loading animation visible to indicate that an action is in progress.
                 *
                 * @param animation The animator that triggered this event.
                 */
                public void onAnimationStart(Animator animation) {
                    joinEventLoadingAnimation.setVisibility(View.VISIBLE);
                }

                /**
                 * This method is called when the animation ends.
                 * It hides the loading animation once the animation is complete, indicating the action has finished.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationEnd(Animator animation) {
                    joinEventLoadingAnimation.setVisibility(View.GONE);
                }

                /**
                 * This method is called if the animation is canceled before it finishes.
                 * It ensures that the loading animation is hidden in case the animation does not complete.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationCancel(Animator animation) {
                    joinEventLoadingAnimation.setVisibility(View.GONE);
                }

                /**
                 * This method is called when the animation repeats.
                 * Since no action is required during the repetition of the animation, this method is left empty.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            lotteryAnimation.removeAllAnimatorListeners();
            // Handles the visibility and button states during the lifecycle of an animation.
            lotteryAnimation.addAnimatorListener(new Animator.AnimatorListener() {

                /**
                 * This method is called when the animation starts.
                 * It makes the lottery animation visible to indicate the start of the event processing.
                 *
                 * @param animation The animator that triggered this event.
                 */
                public void onAnimationStart(Animator animation) {
                    lotteryAnimation.setVisibility(View.VISIBLE);
                }

                /**
                 * This method is called when the animation ends.
                 * It hides the lottery animation, enables the action buttons, and starts a new activity to show the sampled entrants.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationEnd(Animator animation) {

                    lotteryAnimation.setVisibility(View.GONE);
                    btnSampleUsers.setEnabled(true);
                    btnJoinLeaveWaitingList.setEnabled(true);
                    deleteEventButton.setEnabled(true);

                    Intent intent = new Intent(EventView.this, SampledEntrants.class);
                    intent.putExtra("eventId", event.getEventId());
                    startActivity(intent);
                }

                /**
                 * This method is called if the animation is canceled before it completes.
                 * It hides the lottery animation in case of cancellation to prevent lingering UI elements.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationCancel(Animator animation) {
                    lotteryAnimation.setVisibility(View.GONE);
                }

                /**
                 * This method is called when the animation repeats.
                 * Since no action is required when the animation repeats, this method is left empty.
                 *
                 * @param animation The animator that triggered this event.
                 */
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            deleteEventButton.setVisibility(View.VISIBLE);
            deleteEventButton.setOnClickListener(v-> {
                deleteEventButton.setEnabled(false);
                btnJoinLeaveWaitingList.setEnabled(false);
                btnSampleUsers.setEnabled(false);

                progressBar.setVisibility(View.VISIBLE);
                eventUtils.removeEvent(eventId, success-> {
                    progressBar.setVisibility(View.GONE);
                    if (success) {
                        deleteEventAnimation.setVisibility(View.VISIBLE);
                        deleteEventAnimation.playAnimation();
                        joinEventAnimation.setVisibility(View.GONE);
                        joinEventLoadingAnimation.setVisibility(View.GONE);

                         // This listener handles the animation lifecycle events for the "Delete Event Animation" during event deletion.
                         // It ensures that the animation visibility is managed properly and the activity finishes after the animation completes.
                        deleteEventAnimation.addAnimatorListener(new Animator.AnimatorListener() {

                            /**
                             * This method is called when the animation starts.
                             * It makes the delete event animation visible to indicate that the deletion process is beginning.
                             *
                             * @param animation The animator that triggered this event.
                             */
                            public void onAnimationStart(Animator animation) {
                                deleteEventAnimation.setVisibility(View.VISIBLE);
                            }

                            /**
                             * This method is called when the animation ends.
                             * It hides the delete event animation and finishes the activity to indicate that the deletion process is complete.
                             *
                             * @param animation The animator that triggered this event.
                             */
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                deleteEventAnimation.setVisibility(View.GONE);
                                finish();
                            }

                            /**
                             * This method is called if the animation is canceled before it completes.
                             * It hides the delete event animation in case of cancellation to prevent lingering UI elements.
                             *
                             * @param animation The animator that triggered this event.
                             */
                            @Override
                            public void onAnimationCancel(Animator animation) {
                                deleteEventAnimation.setVisibility(View.GONE);
                            }

                            /**
                             * This method is called when the animation repeats.
                             * Since no action is required when the animation repeats, this method is left empty.
                             *
                             * @param animation The animator that triggered this event.
                             */
                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });

                    } else {
                        deleteEventButton.setEnabled(true);
                        btnJoinLeaveWaitingList.setEnabled(true);
                        btnSampleUsers.setEnabled(true);
                        Toast.makeText(this, "Event did not delete", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            btnEditEvent.setVisibility(View.GONE);
            entrantsSlider.setVisibility(View.GONE);
            deleteEventButton.setVisibility(View.GONE);
            deleteImageButton.setVisibility(View.GONE);
        }

        qrCodeButton.setOnClickListener(v -> {
            DocumentReference userRef = db.collection("users").document(deviceID);
            userRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                            if (Boolean.TRUE.equals(isAdmin) && Objects.equals(type, "admin")) {
                                Intent intent = new Intent(this, QRViewAdmin.class);
                                intent.putExtra("Event Id", event.getEventId());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(this, QRView.class);
                                intent.putExtra("Event Id", event.getEventId());
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(this, QRView.class);
                            intent.putExtra("Event Id", event.getEventId());
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Can't find document", e));
        });
    }

    /**
     * Loads an image from a URL into an ImageView.
     *
     * @param imageUrl   The URL of the image.
     * @param placeholder The ImageView to display the image in.
     */
    private void loadImage(String imageUrl, ImageView placeholder) {
        BitmapUtils bitmapUtils = new BitmapUtils();
        if (imageUrl != null ) {
            Bitmap imgBitMap = bitmapUtils.decodeBase64ToBitmap(imageUrl);
            Glide.with(this).load(imgBitMap).into(placeholder);
            if(type == "admin") deleteImageButton.setVisibility(View.VISIBLE);
        } else {
            placeholder.setImageResource(R.drawable.event_place_holder);
            deleteImageButton.setVisibility(View.GONE);
        }
    }

    /**
     * Sets up navigation between different entrant lists.
     *
     * @param eventId The unique identifier of the event.
     */
    private void setupEntrantNavigation(String eventId) {
        TextView currentOptionText = findViewById(R.id.currentOptionText);
        TextView leftArrowButton = findViewById(R.id.leftArrowButton);
        TextView rightArrowButton = findViewById(R.id.rightArrowButton);

        updateDisplay(currentOptionText, eventId);

        leftArrowButton.setOnClickListener(v -> {
            currentOptionIndex = (currentOptionIndex - 1 + entrantOptions.length) % entrantOptions.length;
            updateDisplay(currentOptionText, eventId);
        });


        rightArrowButton.setOnClickListener(v -> {
            currentOptionIndex = (currentOptionIndex + 1) % entrantOptions.length;
            updateDisplay(currentOptionText, eventId);
        });
    }

    /**
     * Sets up navigation between different entrant lists.
     *
     * @param Entrants An array of device IDs representing entrants.
     * @param event    The unique identifier of the event.
     */
    private void showMessageInputDialog(Context context, ArrayList<String> Entrants, Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Write message");
        builder.setMessage("Enter message");
        final EditText input = new EditText(this);
        input.setHint("Enter Message...");
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String message = input.getText().toString().trim();
            if (!message.isEmpty()) {
                event.sendNotifications(context, message, Entrants);
                sendNotifAnimation.setVisibility(View.VISIBLE);
                sendNotifAnimation.setMinAndMaxProgress(0.3f, 1f);
                sendNotifAnimation.playAnimation();
                // This listener handles the animation lifecycle events for the "Send Notification Animation."
                // It ensures that the animation visibility is properly managed during the notification sending process.
                sendNotifAnimation.addAnimatorListener(new Animator.AnimatorListener() {

                    /**
                     * This method is called when the animation starts.
                     * It makes the send notification animation visible to indicate that the notification sending process is starting.
                     *
                     * @param animation The animator that triggered this event.
                     */
                    public void onAnimationStart(Animator animation) {
                        sendNotifAnimation.setVisibility(View.VISIBLE);
                    }

                    /**
                     * This method is called when the animation ends.
                     * It hides the send notification animation once the notification sending process is complete.
                     *
                     * @param animation The animator that triggered this event.
                     */
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        sendNotifAnimation.setVisibility(View.GONE);
                    }

                    /**
                     * This method is called if the animation is canceled before it completes.
                     * It ensures the send notification animation is hidden in case of cancellation.
                     *
                     * @param animation The animator that triggered this event.
                     */
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        sendNotifAnimation.setVisibility(View.GONE);
                    }

                    /**
                     * This method is called when the animation repeats.
                     * Since no action is required when the animation repeats, this method is left empty.
                     *
                     * @param animation The animator that triggered this event.
                     */
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            else {
                Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    /**
     * Updates the displayed entrant list based on the current option index.
     *
     * @param currentOptionText The TextView showing the current option.
     * @param eventId           The unique identifier of the event.
     */
    private void updateDisplay(TextView currentOptionText, String eventId) {
        currentOptionText.setText(entrantOptions[currentOptionIndex]);

        currentOptionText.setOnClickListener(v -> {
            Intent intent;

            switch (entrantOptions[currentOptionIndex]) {
                case "Waitlisted Entrants":
                    intent = new Intent(this, WaitlistedEntrants.class);
                    break;
                case "Selected Entrants":
                    intent = new Intent(this, SampledEntrants.class);
                    break;
                case "Registered Entrants":
                    intent = new Intent(this, EnrolledEntrants.class);
                    break;
                case "Cancelled Entrants":
                    intent = new Intent(this, CancelledEntrants.class);
                    break;
                default:
                    return;
            }

            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });
    }

    /**
     * Cleans up resources when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            eventListener.remove();
        }
    }

    /**
     * Removes the event's picture by resetting the image view to a placeholder and updating the event's image URL in Firestore.
     * It also hides the delete button and shows a Toast message to indicate success or failure.
     */
    private void removePicture() {
        eventPosterImageView.setImageResource(R.drawable.event_place_holder);
        deleteImageButton.setVisibility(View.GONE);
        db.collection("events").document(eventId)
                .update("imageUrl", null)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event picture removed successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to remove event picture.", Toast.LENGTH_SHORT).show();

                });
    }
}