package com.example.zenithevents.Events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiView;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Arrays;


/**
 * CreationSuccessActivity is an activity that is displayed after successfully creating an event.
 * It shows the event's title, image, and a generated QR code, and allows the user to share the QR code.
 * <p>
 * Note: The Javadocs for this class were generated with the assistance of an AI language model.
 * </p>
 */
public class CreationSuccessActivity extends AppCompatActivity {
    private TextView eventNameText;
    private ImageView eventImageView, qrCodeView;
    private Button shareQRButton, exitButton;
    Event event;
    Bitmap QRCode, eventImage;
    ConfettiView confettiView;
    FrameLayout confettiLayout;

    /**
     * Called when the activity is first created. Initializes the UI components, retrieves the Event object passed
     * from the previous activity, and sets up the event name, image, and QR code.
     * It also sets listeners for the buttons to allow sharing the QR code and exiting the activity.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     *                           If the activity has never been created, this will be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_success);

        eventNameText = findViewById(R.id.eventNameText);
        eventImageView = findViewById(R.id.eventImageView);
        qrCodeView = findViewById(R.id.qrCodeView);
        shareQRButton = findViewById(R.id.shareQRButton);
        exitButton = findViewById(R.id.exitButton);
        confettiView = findViewById(R.id.confettiView);
        confettiLayout = findViewById(R.id.confettiLayout);

        event = (Event) getIntent().getSerializableExtra("Event");

        confettiLayout.post(() -> {
            CommonConfetti.rainingConfetti(
                    confettiLayout,
                    new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}
            ).stream(3000);
        });

        if (event != null) {
            eventNameText.setText(event.getEventTitle());

            if (event.getImageUrl() != null) {
                eventImage = QRCodeUtils.decodeBase64ToBitmap(event.getImageUrl());
                eventImageView.setImageBitmap(eventImage);
            }

            if (getIntent().getStringExtra("qr_code") != null) {
                QRCode = QRCodeUtils.decodeBase64ToBitmap(getIntent().getStringExtra("qr_code"));
                qrCodeView.setImageBitmap(QRCode);
            }

            shareQRButton.setOnClickListener(v -> {
                shareQRCode(QRCode);
            });

            exitButton.setOnClickListener(v -> {
                finish();
            });
        }
    }

    /**
     * Shares the QR code bitmap as an image using an intent.
     * The image is inserted into the device's media store, and an intent is launched to allow the user to share the image.
     *
     * @param qrCodeBitmap The Bitmap representation of the QR code to be shared.
     */
    private void shareQRCode(Bitmap qrCodeBitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "QR Code", "Event QR Code");
        Uri qrCodeUri = Uri.parse(path);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
        startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
    }
}
