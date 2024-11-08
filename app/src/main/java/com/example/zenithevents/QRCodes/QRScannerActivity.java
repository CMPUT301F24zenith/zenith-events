package com.example.zenithevents.QRCodes;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zenithevents.HelperClasses.QRCodeUtils;
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

    private void checkForEvent(String qrCodeContent) {
        String qrCodeHashed = QRCodeUtils.hashQRCodeData(qrCodeContent);

        db.collection("events").whereEqualTo("qrcodeHash", qrCodeHashed).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                String eventId = documentSnapshot.getId();

                Intent intent = new Intent(QRScannerActivity.this, EventView.class);
                intent.putExtra("event_id", eventId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(QRScannerActivity.this, "Invalid QR code or event not found.", Toast.LENGTH_SHORT).show();
                finish();
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