package com.example.zenithevents.Events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.google.zxing.qrcode.encoder.QRCode;

public class CreationSuccessActivity extends AppCompatActivity {
    private TextView eventNameText;
    private ImageView eventImageView, qrCodeView;
    private Button shareQRButton;
    Event event;
    Bitmap QRCode, eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_success);

        eventNameText = findViewById(R.id.eventNameText);
        eventImageView = findViewById(R.id.eventImageView);
        qrCodeView = findViewById(R.id.qrCodeView);
        shareQRButton = findViewById(R.id.shareQRButton);

        event = (Event) getIntent().getSerializableExtra("Event");

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
        }
    }

    private void shareQRCode(Bitmap qrCodeBitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "QR Code", "Event QR Code");
        Uri qrCodeUri = Uri.parse(path);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
        startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
    }
}
