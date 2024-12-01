package com.example.zenithevents.Events;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.zenithevents.HelperClasses.EventUtils;
import android.Manifest;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Activity that allows the creation and editing of event details, including uploading a poster image and generating a QR code.
 * <p>Note: The Javadocs for this class were generated with the assistance of an AI language model.</p>
 */
public class CreateEventPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    Button createEventSaveButton, createEventCancelButton, uploadEventPosterButton;
    String eventName, selectedLimitString, eventLimitString, numParticipants, uploadedPosterString, eventLocation, eventDescription;
    TextView pageTitleView;
    EditText eventNameView, eventLimitView, eventLocationView, eventDescriptionView, selectedLimitView;
    CheckBox geolocationCheck;
    Boolean isGeolocationChecked;
    String eventId;
    ImageView eventPosterImage;
    Uri uploadedPosterUri;
    Event event;
    private EventUtils eventUtils;

    /**
     * Called when the activity is created. Initializes the UI elements and loads the event details if available.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_main);
        Log.d("FunctionCall", "11,3");
        eventUtils = new EventUtils();
        Log.d("FunctionCall", "11,4");
        eventId = getIntent().getStringExtra("Event Id");
        Log.d("FunctionCall", "11,6");

        pageTitleView = findViewById(R.id.createEventTitle);
        eventNameView = findViewById(R.id.eventNameInput);
        eventLimitView = findViewById(R.id.eventLimitInput);
        eventLocationView = findViewById(R.id.eventLocationInput);
        eventDescriptionView = findViewById(R.id.eventDescriptionInput);
        eventPosterImage = findViewById(R.id.eventPosterImage);
        createEventSaveButton = findViewById(R.id.createEventSaveButton);
        createEventCancelButton = findViewById(R.id.createEventCancelButton);
        uploadEventPosterButton = findViewById(R.id.uploadEventPosterButton);
        selectedLimitView = findViewById(R.id.selectedLimitInput);
        geolocationCheck = findViewById(R.id.geolocationCheckbox);

        event = new Event();

        if (!eventId.isEmpty()) {
            eventUtils.fetchEventById(eventId, event_ -> {
                event = event_;

                Log.d("FunctionCall", "EVENT NULL... test");
                uploadedPosterString = event.getImageUrl();
                if (uploadedPosterString != null) {
                    Bitmap decodedImage = QRCodeUtils.decodeBase64ToBitmap(uploadedPosterString);
                    Glide.with(this).load(decodedImage).into(eventPosterImage);
                }

                pageTitleView.setText("Edit Event");

                eventName = event.getEventName();
                eventNameView.setText(eventName);

                eventDescription = event.getEventDescription();
                eventDescriptionView.setText(eventDescription);

                eventLocation = event.getEventAddress();
                eventLocationView.setText(eventLocation);

                geolocationCheck.setChecked(event.getHasGeolocation());
                geolocationCheck.setEnabled(false);

                selectedLimitView.setText(String.valueOf(event.getSelectedLimit()));
                selectedLimitView.setEnabled(false);

                eventLimitView.setText(String.valueOf(event.getNumParticipants()));
                eventLimitView.setEnabled(false);
            });
        } else {
            pageTitleView.setText("Create Event");
        }

        createEventSaveButton.setOnClickListener(v -> {
            eventName = eventNameView.getText().toString();
            numParticipants = eventLimitView.getText().toString();
            eventLocation = eventLocationView.getText().toString();
            eventDescription = eventDescriptionView.getText().toString();
            selectedLimitString = selectedLimitView.getText().toString();

            event.setEventName(eventName);
            event.setEventDescription(eventDescription);
            event.setEventAddress(eventLocation);

            if (!eventId.isEmpty()) {
                event.setHasGeolocation(geolocationCheck.isChecked());

                if (!numParticipants.isEmpty()) {
                    event.setNumParticipants(Integer.parseInt(numParticipants));
                } else {
                    event.setNumParticipants(0);
                }

                if (!selectedLimitString.isEmpty()) {
                    event.setSelectedLimit(Integer.parseInt(selectedLimitString));
                } else {
                    event.setSelectedLimit(0);
                }
            } else {
                if (Objects.equals(numParticipants, "0")) {
                    eventLimitView.setError("Limit can't be 0");
                    eventLimitView.requestFocus();
                    return;
                }

                if (Objects.equals(selectedLimitString, "0")) {
                    selectedLimitView.setError("Selected Limit can't be 0");
                    selectedLimitView.requestFocus();
                    return;
                }
            }

            Log.d("FunctionCall1", "---" + event.getSelectedLimit());

            if (Objects.equals(eventName, "")) {
                eventNameView.setError("Event Name can't be empty");
                eventNameView.requestFocus();
                return;
            }

            Log.d("FunctionCall", "if1");

            if (uploadedPosterUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uploadedPosterUri);
                    Log.d("FunctionCall", "inputStream: " + inputStream);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    uploadedPosterString = QRCodeUtils.encodeBitmapToBase64(image);
                    event.setImageUrl(uploadedPosterString);
                } catch (FileNotFoundException e) {
                    Log.d("DEBUG", "Image couldn't be processed");
                }
            }

            Log.d("FunctionCall", "clicked....");
            eventUtils.createUpdateEvent(this, event, eventId -> {
                if (eventId != null) {
                    event.setEventId(eventId);
                    Log.d("FunctionCall", "if2.5");
                    String qrCodeBase64;
                    if (event.getQRCodeHash() == null) {
                        String qrCodeContent = QRCodeUtils.generateRandomString(16);
                        Bitmap qrCodeBitmap = QRCodeUtils.generateQRCode(qrCodeContent);
                        qrCodeBase64 = QRCodeUtils.encodeBitmapToBase64(qrCodeBitmap);
                        if (qrCodeBase64 != null) {
                            event.setQRCodeBitmap(qrCodeBase64);
                        }
                        String qrCodeHashed = QRCodeUtils.hashQRCodeData(qrCodeContent);
                        if (qrCodeHashed != null) {
                            event.setQRCodeHash(qrCodeHashed);
                        }
                        Log.d("FunctionCall", "if3");
                    } else {
                        qrCodeBase64 = event.getQRCodeBitmap();
                    }

                    eventUtils.createUpdateEvent(this, event, eventId_ -> {
                        if (eventId_ != null) {
                            Log.d("Firestore", "QR code hash updated successfully.");
                            Toast.makeText(this, "Event was successfully published!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateEventPage.this, CreationSuccessActivity.class);
                            intent.putExtra("Event", event);
                            intent.putExtra("qr_code", qrCodeBase64);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "There was an error updating the QR code. Please try again!", Toast.LENGTH_SHORT).show();
                            Log.w("Firestore", "Failed to update QR code hash.");
                        }
                    });
                } else {
                    Toast.makeText(this, "There was an error. Please try again!", Toast.LENGTH_SHORT).show();
                    Log.d("FunctionCall", "if2.6");
                }
            });
        });

        createEventCancelButton.setOnClickListener(v -> finish());

        uploadEventPosterButton.setOnClickListener(v -> checkStoragePermission());
    }

    /**
     * Opens the device's file picker to select an image.
     */
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    /**
     * Handles the result of selecting an image from the file picker.
     *
     * @param requestCode The request code passed to startActivityForResult.
     * @param resultCode The result code from the activity.
     * @param data The data returned from the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadedPosterUri = data.getData();
            eventPosterImage.setImageURI(uploadedPosterUri);
        }
    }

    /**
     * Checks if the app has permission to read images from storage, and requests permission if necessary.
     */
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PICK_IMAGE);
        } else {
            openImage();
        }
    }

    /**
     * Handles the result of requesting permissions.
     *
     * @param requestCode The request code passed to requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
