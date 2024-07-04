package com.bestofluck.qrcode;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
public class scanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Button scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(v -> startQRScanner());
    }

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                handleQRCode(result.getContents());
            }
        }
    }

    private void handleQRCode(String qrCodeContent) {
        // Check if the QR code content is a URL
        if (qrCodeContent.startsWith("http://") || qrCodeContent.startsWith("https://")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrCodeContent));
            startActivity(browserIntent);
        } else {
            // Show the content of the QR code
            Toast.makeText(this, qrCodeContent, Toast.LENGTH_LONG).show();
        }
    }
}