package com.example.zenithevents.QRCodes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zenithevents.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class QRScannerActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);
        barcodeView = findViewById(R.id.barcodeView);
        capture = new CaptureManager(this, barcodeView);


        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                String qrCodeContent = result.getText();
                Toast.makeText(QRScannerActivity.this, "Scanned: " + qrCodeContent, Toast.LENGTH_LONG).show();
                capture.onPause();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("SCAN_RESULT", qrCodeContent);
                setResult(RESULT_OK, resultIntent);
                finish();
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }
}