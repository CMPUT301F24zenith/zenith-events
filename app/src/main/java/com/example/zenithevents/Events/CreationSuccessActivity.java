package com.example.zenithevents.Events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zenithevents.HelperClasses.BitmapUtils;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.Objects.Event;
import com.example.zenithevents.R;
import com.example.zenithevents.User.OrganizerPage;
import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * CreationSuccessActivity is an activity that is displayed after successfully creating an event.
 * It shows the event's title, image, and a generated QR code, and allows the user to share the QR code.
 * <p>
 * Note: The Javadocs for this class were generated with the assistance of an AI language model.
 * </p>
 */
public class CreationSuccessActivity extends AppCompatActivity {
    private TextView successfulText, eventName, eventFacility;
    private ImageView eventImageView, qrCodeView;
    private Button shareQRButton, exitButton;
    String eventID, eventNameString, eventFacilityString, eventBitmap;
    Bitmap QRCode, eventImage;
    androidx.cardview.widget.CardView cardView;
    ConfettiView confettiView;
    FrameLayout confettiLayout;
    FirebaseFirestore db;

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

        successfulText = findViewById(R.id.successfulText);
        qrCodeView = findViewById(R.id.qrCodeView);
        shareQRButton = findViewById(R.id.shareQRButton);
        exitButton = findViewById(R.id.exitButton);
        confettiView = findViewById(R.id.confettiView);
        confettiLayout = findViewById(R.id.confettiLayout);
        ImageButton acceptBtn = findViewById(R.id.acceptEventBtn);
        ImageButton declineBtn = findViewById(R.id.declineEventBtn);
        eventName = findViewById(R.id.eventTitle);
        eventFacility = findViewById(R.id.facilityName);
        eventImageView = findViewById(R.id.eventImage);

        eventID = getIntent().getStringExtra("eventID");
        eventNameString = getIntent().getStringExtra("eventName");
        eventFacilityString = getIntent().getStringExtra("eventFacility");

        db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            eventBitmap = documentSnapshot.getString("imageUrl");
                            eventName.setText(eventNameString);
                            eventFacility.setText(eventFacilityString);

                            if (eventBitmap != null) {
                                Bitmap eventTrueBitmap = BitmapUtils.decodeBase64ToBitmap(eventBitmap);
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), eventTrueBitmap);
                                Glide.with(this)
                                        .load(bitmapDrawable)
                                        .into(eventImageView);
                            } else {
                                eventImageView.setImageResource(R.drawable.event_place_holder);
                            }
                        }
                    }
                });

        confettiLayout.post(() -> {
            CommonConfetti.rainingConfetti(
                    confettiLayout,
                    new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}
            ).stream(3000);
        });

        String qrCodeBase64 = getIntent().getStringExtra("qr_code");
        if (qrCodeBase64 != null) {
            QRCode = QRCodeUtils.decodeBase64ToBitmap(qrCodeBase64);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), QRCode);
            Glide.with(this)
                    .load(bitmapDrawable)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(qrCodeView);
        }

        acceptBtn.setVisibility(View.GONE);
        declineBtn.setVisibility(View.GONE);

        shareQRButton.setOnClickListener(v -> shareQRCode(QRCode));

        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrganizerPage.class);
            startActivity(intent);
            finish();
        });
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