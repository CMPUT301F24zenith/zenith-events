package com.example.zenithevents.Organizer;

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

public class QRView extends AppCompatActivity {
    private TextView eventTitleText;
    private ImageView qrCodeView;
    private Button shareQRButton, doneButton;
    Event event;
    Bitmap qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_view);

        eventTitleText = findViewById(R.id.eventTitle);
        qrCodeView = findViewById(R.id.qrCodeImageView);
        shareQRButton = findViewById(R.id.shareQRCodeButton);
        doneButton = findViewById(R.id.doneButton);

        event = (Event) getIntent().getSerializableExtra("Event");

        if (event != null) {
            eventTitleText.setText(event.getEventTitle());

            qrCode = QRCodeUtils.decodeBase64ToBitmap(event.getQRCodeBitmap());
            qrCodeView.setImageBitmap(qrCode);

            shareQRButton.setOnClickListener(v -> {
                shareQRCode(qrCode);
            });

            doneButton.setOnClickListener(v -> {
                finish();
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
