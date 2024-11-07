package com.example.zenithevents.QRCodes;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zenithevents.Organizer.EventView;
import com.example.zenithevents.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import android.content.Intent;
import android.widget.Toast;
import android.Manifest;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScannerActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private FirebaseFirestore db;

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

    private void checkForEvent(String eventId) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Intent intent = new Intent(QRScannerActivity.this, EventView.class);
                    intent.putExtra("event_id", eventId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(QRScannerActivity.this, "Please scan a valid QR code.", Toast.LENGTH_SHORT).show();
                    barcodeView.resume();
                }
            } else {
                Toast.makeText(QRScannerActivity.this, "Failed to connect to database.", Toast.LENGTH_SHORT).show();
                barcodeView.resume();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

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