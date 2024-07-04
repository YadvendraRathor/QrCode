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
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.OutputStream;

public class generate extends AppCompatActivity {

    private EditText editTextInput;
    private Button buttonGenerate;
    private ImageView imageViewQRCode;
    private Button buttonShare;
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        editTextInput = findViewById(R.id.editTextInput);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        buttonShare = findViewById(R.id.buttonShare);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    try {
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 400, 400);
                        qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageViewQRCode.setImageBitmap(qrCodeBitmap);
                        buttonShare.setVisibility(View.VISIBLE);
                        saveImageToGallery(qrCodeBitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(generate.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(generate.this, "Please enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }

    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + System.currentTimeMillis() + ".png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                if (outputStream != null) {
                    outputStream.close();
                    Toast.makeText(this, "QR Code saved to gallery", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage() {
        if (qrCodeBitmap != null) {
            Uri uri = getImageUri(qrCodeBitmap);
            if (uri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/png");
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
            }
        } else {
            Toast.makeText(this, "No QR Code to share", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_Share_" + System.currentTimeMillis() + ".png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                if (outputStream != null) {
                    outputStream.close();
                    return uri;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}