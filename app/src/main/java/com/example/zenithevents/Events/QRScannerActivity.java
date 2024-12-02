package com.example.zenithevents.Events;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zenithevents.Events.EventView;
import com.example.zenithevents.HelperClasses.QRCodeUtils;
import com.example.zenithevents.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import android.content.Intent;
import android.widget.Toast;
import android.Manifest;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Activity that provides QR code scanning functionality for scanning event QR codes.
 * <p>This activity uses the camera to scan QR codes and checks the scanned code against a Firestore database to find matching events. If a valid event is found, the user is directed to the event details screen. If the QR code is invalid or the event is not found, a toast message is displayed to the user.</p>
 * <p>Note: The JavaDocs for this class were generated using OpenAI's ChatGPT.</p>
 * <p>The methods in this class enable the following functionality:</p>
 * <ul>
 *     <li>Requesting camera permission if it is not already granted.</li>
 *     <li>Scanning QR codes continuously using a barcode scanner.</li>
 *     <li>Hashing the QR code content and checking it against the "events" collection in Firestore.</li>
 *     <li>Redirecting the user to the event details screen upon successfully finding a matching event.</li>
 *     <li>Displaying an error message if the QR code is invalid or no event is found.</li>
 * </ul>
 */
public class QRScannerActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private FirebaseFirestore db;

    /**
     * Called when the activity is first created. Initializes the activity, checks for camera permissions,
     * and starts the QR code scanning process if permission is granted.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     *                           If the activity has never been created, this will be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        barcodeView = findViewById(R.id.barcodeView);
        db = FirebaseFirestore.getInstance();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            initializeScanner();
        }
    }

    /**
     * Checks if the scanned QR code matches any event in the Firestore database.
     * The QR code content is hashed using SHA-256 and compared with the hashes stored in the database.
     * If a match is found, the user is redirected to the event details page.
     * If no match is found, a toast message is displayed to inform the user.
     *
     * @param qrCodeContent The content of the scanned QR code, which represents the event.
     */
    private void checkForEvent(String qrCodeContent) {
        String qrCodeHashed = QRCodeUtils.hashQRCodeData(qrCodeContent);

        db.collection("events").whereEqualTo("qrcodeHash", qrCodeHashed).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                String eventId = documentSnapshot.getId();

                Intent intent = new Intent(QRScannerActivity.this, EventView.class);
                intent.putExtra("event_id", eventId);
                intent.putExtra("type", "waitingEvents");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(QRScannerActivity.this, "Invalid QR code or event not found.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Called when the activity is resumed. Resumes the QR code scanning process.
     */
    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    /**
     * Called when the activity is paused. Pauses the QR code scanning process to save resources.
     */
    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    /**
     * Initializes the barcode scanner and sets up a continuous decoding process.
     * Once a QR code is scanned, the `barcodeResult` method is called, which then checks for an event.
     */
    private void initializeScanner() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String qrCodeContent = result.getText();
                barcodeView.pause();
                checkForEvent(qrCodeContent);
            }
        });
    }
}