package com.example.zenithevents.Events;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
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
import com.example.zenithevents.User.OrganizerPage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CreateEventPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    Button createEventSaveButton, createEventCancelButton, uploadEventPosterButton;
    String pageTitle, eventTitle, eventLimitString, numParticipants, uploadedPosterString, eventLocation, eventDescription;
    TextView pageTitleView;
    EditText eventNameView, eventLimitView, eventLocationView, eventDescriptionView;
    Event event;
    ImageView eventPosterImage;
    Uri uploadedPosterUri;
    private EventUtils eventUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_main);
        eventUtils = new EventUtils();

        pageTitle = getIntent().getStringExtra("page_title");
        event = (Event) getIntent().getSerializableExtra("Event");

        assert event != null;

        eventPosterImage = findViewById(R.id.eventPosterImage);
        uploadedPosterString = event.getImageUrl();
        if (uploadedPosterString != null) {
            Bitmap decodedImage = QRCodeUtils.decodeBase64ToBitmap(uploadedPosterString);
            Glide.with(this).load(decodedImage).into(eventPosterImage);
        }


        pageTitleView = findViewById(R.id.createEventTitle);
        pageTitleView.setText(pageTitle);

        eventTitle = event.getEventTitle();
        eventNameView = findViewById(R.id.eventNameInput);
        eventNameView.setText(eventTitle);

        eventLimitString = event.getNumParticipants() == 0 ? "" : String.valueOf(event.getNumParticipants());
        eventLimitView = findViewById(R.id.eventLimitInput);
        eventLimitView.setText(eventLimitString);

        eventDescription = event.getEventDescription();
        eventDescriptionView = findViewById(R.id.eventDescriptionInput);
        eventDescriptionView.setText(eventDescription);

        eventLocation = event.getEventAddress();
        eventLocationView = findViewById(R.id.eventLocationInput);
        eventLocationView.setText(eventLocation);


        createEventSaveButton = findViewById(R.id.createEventSaveButton);
        createEventSaveButton.setOnClickListener(v -> {
            Log.d("FunctionCall", "createEventSaveButton");

            eventTitle = eventNameView.getText().toString();
            numParticipants = String.valueOf(eventLimitView.getText());
            eventLocation = eventLocationView.getText().toString();
            eventDescription = eventDescriptionView.getText().toString();

            Context context = CreateEventPage.this;

            if (Objects.equals(eventTitle, "")) {
                eventNameView.setError("Event Name can't be empty");
                eventNameView.requestFocus();
            }

            if (Objects.equals(numParticipants, "0")) {
                eventLimitView.setError("Limit can't be 0");
                eventLimitView.requestFocus();
            }

            if (!Objects.equals(numParticipants, "0") && !Objects.equals(eventTitle, "")) {
                Log.d("FunctionCall", "if1");

                event.setEventTitle(eventTitle);
                event.setNumParticipants(Objects.equals(numParticipants, "") ? 0 : Integer.parseInt(numParticipants));
                event.setEventDescription(eventDescription);
                event.setEventAddress(eventLocation);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

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

              eventUtils.createUpdateEvent(context, event, eventId -> {
                    if (eventId != null) {
                        event.setEventId(eventId);
                        Log.d("FunctionCall", "if2.5");

                        String qrCodeContent = QRCodeUtils.generateRandomString(16);
                        Bitmap qrCodeBitmap = QRCodeUtils.generateQRCode(qrCodeContent);
                        String qrCodeBase64 = QRCodeUtils.encodeBitmapToBase64(qrCodeBitmap);
                        if (qrCodeBase64 != null) {
                            event.setQRCodeBitmap(qrCodeBase64);
                        }
                        String qrCodeHashed = QRCodeUtils.hashQRCodeData(qrCodeContent);
                        if (qrCodeHashed != null) {
                            event.setQRCodeHash(qrCodeHashed);
                        }
                        Log.d("FunctionCall", "if3");


                        eventUtils.createUpdateEvent(context, event, eventId_ -> {
                            if (eventId_ != null) {
                                Log.d("Firestore", "QR code hash updated successfully.");
                                Toast.makeText(this, "Event was successfully published!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CreateEventPage.this, CreationSuccessActivity.class);
                                intent.putExtra("Event", event);
                                intent.putExtra("qr_code", qrCodeBase64);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "There was an error updating the QR code. Please try again!", Toast.LENGTH_SHORT).show();
                                Log.w("Firestore", "Failed to update QR code hash.");
                            }
                        });
                    } else {
                        Toast.makeText(context, "There was an error. Please try again!", Toast.LENGTH_SHORT).show();
                        Log.d("FunctionCall", "if2.6");
                    }
                });
            }
        });

        createEventCancelButton = findViewById(R.id.createEventCancelButton);
        createEventCancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
            startActivity(intent);
        });

        uploadEventPosterButton = findViewById(R.id.uploadEventPosterButton);
        uploadEventPosterButton.setOnClickListener(v -> {
            checkStoragePermission();
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadedPosterUri = data.getData();
            eventPosterImage.setImageURI(uploadedPosterUri);
        }
    }
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PICK_IMAGE);
        } else {
            openImage();
        }
    }

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
