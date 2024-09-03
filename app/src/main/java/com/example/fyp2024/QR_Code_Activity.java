package com.example.fyp2024;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QR_Code_Activity extends AppCompatActivity {
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String USER_VERIFICATIONCODE = "com.example.fyp2024.USER_VERIFICATIONCODE";

    String name;
    String email;
    String userID;
    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(ViewBooking.USERID);
            name = intent.getStringExtra(ViewBooking.USER_NAME);
            email = intent.getStringExtra(ViewBooking.USER_EMAIL);
            verificationCode = intent.getStringExtra(ViewBooking.USER_VERIFICATIONCODE);

            Log.d("message", "userIDl!"+userID);

        } else {
            Toast.makeText(QR_Code_Activity.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

        ImageView qrCodeImageView = findViewById(R.id.QR_Code_ImageView);

        String text = verificationCode;  //QR code text

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void QR_CodeBackBtn(View v) {
        Log.d("QR_code","QR_code Back Btn called");

        Intent intent = new Intent(QR_Code_Activity.this, ViewBooking.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }
}