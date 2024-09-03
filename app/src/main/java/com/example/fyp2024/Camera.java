package com.example.fyp2024;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.fyp2024.DB.BitmapSingleton;

public class Camera extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";

    String name;
    String email;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(VerificationCode_Camera.STAFF_USERID);
            name = intent.getStringExtra(VerificationCode_Camera.STAFF_USER_NAME);
            email = intent.getStringExtra(VerificationCode_Camera.STAFF_USER_EMAIL);
//            user_text.setText("Hi, "+name+"!");
//            Toast.makeText(Home.this, "Id!"+userID, Toast.LENGTH_SHORT).show();
        }

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            BitmapSingleton.getInstance().setBitmap(imageBitmap);

            Intent intent = new Intent(Camera.this, VerificationCode_Camera.class);
            intent.putExtra(STAFF_USERID, userID);
            intent.putExtra(STAFF_USER_NAME, name);
            intent.putExtra(STAFF_USER_EMAIL, email);
            startActivity(intent);
            // *********
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}