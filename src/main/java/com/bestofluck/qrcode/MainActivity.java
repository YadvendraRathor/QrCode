package com.bestofluck.qrcode;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Button buttonCreateQRCode;
    private Button buttonScanQRCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCreateQRCode = findViewById(R.id.buttonCreateQRCode);
        buttonScanQRCode = findViewById(R.id.buttonScanQRCode);

        buttonCreateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity to create a QR code
                Intent intent = new Intent(MainActivity.this, generate.class);
                startActivity(intent);
            }
        });

        buttonScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity to create a QR code
                Intent intent = new Intent(MainActivity.this, scanner.class);
                startActivity(intent);
            }
        });
    }

}