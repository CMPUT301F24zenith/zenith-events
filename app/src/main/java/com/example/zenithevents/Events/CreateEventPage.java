package com.example.zenithevents.Events;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.Objects;

public class CreateEventPage extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    Button createEventSaveButton, createEventCancelButton, uploadEventPosterButton;
    String pageTitle, eventName, eventLimitString, eventLimit, uploadedPosterString;
    TextView pageTitleView;
    EditText eventNameView, eventLimitView;
    Event event;
    ImageView eventPosterImage;
    Uri uploadedPosterUrl;
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
        uploadedPosterString = event.getEventImage();
        uploadedPosterUrl = Uri.parse(uploadedPosterString);
        if (event.getEventImage() != null) {
            Glide.with(this)
                    .load(uploadedPosterString)
                    .into(eventPosterImage);
            Log.d("FunctionCall", "" + uploadedPosterUrl);
        }

        eventName = event.getEventName();
        eventLimitString = event.getEventLimit() == 0 ? "" : String.valueOf(event.getEventLimit());

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
                event.setEventLimit(Objects.equals(eventLimit, "") ? 0 : Integer.parseInt(eventLimit));

                eventUtils.updateEvent(context, event, eventId -> {
                    if (eventId != null) {
                        Toast.makeText(context, "Event was successfully published!", Toast.LENGTH_SHORT).show();
                        event.setEventId(eventId);
                    } else {
                        Toast.makeText(context, "There was an error. Please try again!", Toast.LENGTH_SHORT).show();
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
            uploadedPosterUrl = data.getData();
            eventPosterImage.setImageURI(uploadedPosterUrl);
            uploadedPosterString = String.valueOf(uploadedPosterUrl);
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
