package com.example.zenithevents.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.HelperClasses.EventUtils;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;

/**
 * QRView is an activity that displays a QR code for an event, allowing the user to view and share it.
 * <p>
 * This activity retrieves an Event object, displays the event title, and generates a QR code that can be shared with others.
 * It includes a button to share the QR code and a button to close the activity.
 * </p>
 * <p>
 * Note: The Javadocs for this class were generated with the assistance of an AI language model.
 * </p>
 */
public class QRViewAdmin extends AppCompatActivity {
    private TextView eventTitleText;
    private ImageView qrCodeView;
    private Button shareQRButton, doneButton, deleteButton;
    Event event;
    Bitmap qrCode;
    EventUtils eventUtils = new EventUtils();

    /**
     * Called when the activity is first created. Initializes the UI components, retrieves the Event object passed
     * from the previous activity, and sets up event handling for the buttons.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     *                           If the activity has never been created, this will be null.
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

        event = (Event) getIntent().getSerializableExtra("Event");

        if (event != null) {
            eventTitleText.setText(event.getEventName());

            qrCode = QRCodeUtils.decodeBase64ToBitmap(event.getQRCodeBitmap());
            qrCodeView.setImageBitmap(qrCode);

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

                eventUtils.createUpdateEvent(QRViewAdmin.this, event, eventId_ -> {
                    if (eventId_ != null) {
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
