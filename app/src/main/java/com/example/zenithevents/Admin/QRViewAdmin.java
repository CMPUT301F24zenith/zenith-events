package com.example.zenithevents.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

/**
 * QRViewAdmin is an activity that displays the QR code for a specific event,
 * allows sharing the QR code, regenerating a new QR code, and navigating back to the previous screen.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class QRViewAdmin extends AppCompatActivity {
    private TextView eventTitleText;
    private ImageView qrCodeView;
    private Button shareQRButton, doneButton, deleteButton;
    Event event;
    Bitmap qrCode;
    String eventId;
    EventUtils eventUtils = new EventUtils();


    /**
     * Called when the activity is created. Initializes the UI components,
     * retrieves the event data, and sets up button listeners for sharing,
     * deleting, and finishing the activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, then this Bundle contains
     *                           the most recent data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_qr_view);

        eventTitleText = findViewById(R.id.eventTitle);
        qrCodeView = findViewById(R.id.qrCodeImageView);
        shareQRButton = findViewById(R.id.shareQRCodeButton);
        doneButton = findViewById(R.id.doneButton);
        deleteButton = findViewById(R.id.deleteQRCodeButton);

        eventId = getIntent().getStringExtra("Event Id");
        eventUtils.fetchEventById(eventId, event_ -> {
            event = event_;

            if (event != null) {
                eventTitleText.setText(event.getEventName());
            qrCode = QRCodeUtils.decodeBase64ToBitmap(event.getQRCodeBitmap());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), qrCode);
            Glide.with(this)
                    .load(bitmapDrawable)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(qrCodeView);

                shareQRButton.setOnClickListener(v -> {
                    shareQRCode(qrCode);
                });

                doneButton.setOnClickListener(v -> {
                    finish();
                });
                deleteButton.setOnClickListener(v -> {
                    String newQRContent = QRCodeUtils.generateRandomString(16);
                    Bitmap newQRBitmap = QRCodeUtils.generateQRCode(newQRContent);
                    String qrCodeBase64 = QRCodeUtils.encodeBitmapToBase64(newQRBitmap);
                    event.setQRCodeBitmap(qrCodeBase64);
                    String qrCodeHashed = QRCodeUtils.hashQRCodeData(newQRContent);
                    event.setQRCodeHash(qrCodeHashed);

                    eventUtils.createUpdateEvent(QRViewAdmin.this, event, eventId_1 -> {
                        if (eventId_1 != null) {
                            Log.d("Firestore", "QR code hash updated successfully.");
                            Toast.makeText(this, "QR Code was successfully deleted!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "There was an error updating the QR code. Please try again!", Toast.LENGTH_SHORT).show();
                            Log.w("Firestore", "Failed to update QR code hash.");
                        }
                    });
                    finish();
                });
            }
        });
    }

    /**
     * Shares the QR code via an intent. Converts the QR code to a URI and opens a share dialog.
     *
     * @param qrCodeBitmap The bitmap of the QR code to be shared.
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
