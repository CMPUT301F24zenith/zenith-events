package com.example.zenithevents.Events;

import static com.example.zenithevents.HelperClasses.QRCodeUtils.generateQRCode;
import static com.example.zenithevents.HelperClasses.QRCodeUtils.hashQRCodeData;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.zenithevents.R;
import com.example.zenithevents.User.OrganizerPage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class CreateEventPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    Button createEventSaveButton, createEventCancelButton, uploadEventPosterButton;
    String pageTitle, eventName, eventLimitString, eventLimit, uploadedPosterString;
    TextView pageTitleView;
    EditText eventNameView, eventLimitView;
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
            Bitmap decodedImage = decodeBase64ToBitmap(uploadedPosterString);
            Glide.with(this).load(decodedImage).into(eventPosterImage);
        }

        eventName = event.getEventName();
        eventLimitString = event.getNumParticipants() == 0 ? "" : String.valueOf(event.getNumParticipants());

        pageTitleView = findViewById(R.id.createEventTitle);
        pageTitleView.setText(pageTitle);

        eventNameView = findViewById(R.id.eventNameInput);
        eventNameView.setText(eventName);

        eventLimitView = findViewById(R.id.eventLimitInput);
        eventLimitView.setText(eventLimitString);

        createEventSaveButton = findViewById(R.id.createEventSaveButton);
        createEventSaveButton.setOnClickListener(v -> {
            eventName = eventNameView.getText().toString();
            eventLimit = String.valueOf(eventLimitView.getText());
            Context context = CreateEventPage.this;

            if (Objects.equals(eventName, "")) {
                eventNameView.setError("Event Name can't be empty");
                eventNameView.requestFocus();
            }

            if (Objects.equals(eventLimit, "0")) {
                eventLimitView.setError("Limit can't be 0");
                eventLimitView.requestFocus();
            }

            if (!Objects.equals(eventLimit, "0") && !Objects.equals(eventName, "")) {
                event.setEventName(eventName);
                event.setNumParticipants(Objects.equals(eventLimit, "") ? 0 : Integer.parseInt(eventLimit));

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

                assert uploadedPosterUri != null;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uploadedPosterUri);
                    Log.d("FunctionCall", "inputStream: " + inputStream);
                    assert inputStream != null;
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    uploadedPosterString = encodeBitmapToBase64(image);
                    event.setImageUrl(uploadedPosterString);

                    eventUtils.updateEvent(context, event, eventId -> {
                        if (eventId != null) {
                            Toast.makeText(context, "Event was successfully published!", Toast.LENGTH_SHORT).show();
                            event.setEventId(eventId);
                        } else {
                            Toast.makeText(context, "There was an error. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Bitmap qrCodeBitmap = generateQRCode(event.getEventId());
                    String qrCodeBase64 = encodeBitmapToBase64(qrCodeBitmap);
                    String hashedQR = hashQRCodeData(qrCodeBase64);
                    event.setQRCodeURL(hashedQR);


                } catch (FileNotFoundException e) {
                    Log.d("DEBUG", "File not found");
                }
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

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
