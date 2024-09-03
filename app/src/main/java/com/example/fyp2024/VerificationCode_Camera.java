package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.BitmapSingleton;
import com.example.fyp2024.DB.Payment;
import com.example.fyp2024.DB.Slot;
import com.example.fyp2024.DB.VerificationCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;

public class VerificationCode_Camera extends AppCompatActivity {
    String tessdata = "/tessdata";
    String fileNameLang = "eng.traineddata";
    TextView text;
    Bitmap receivedBitmap;

    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";
    String name;
    String email;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_camera);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(StaffHome.STAFF_USERID);
            name = intent.getStringExtra(StaffHome.STAFF_USER_NAME);
            email = intent.getStringExtra(StaffHome.STAFF_USER_EMAIL);
//            user_text.setText("Hi, "+name+"!");
//            Toast.makeText(Home.this, "Id!"+userID, Toast.LENGTH_SHORT).show();
        }

        text = findViewById(R.id.Result);

        receivedBitmap = BitmapSingleton.getInstance().getBitmap();
        if (receivedBitmap != null) {
            ImageView image = findViewById(R.id.imageView);
            image.setImageBitmap(receivedBitmap);

            StartTess();
            TessBaseAPI mTess = new TessBaseAPI();
            String datapath = getExternalFilesDir("/").getPath() + "/";
            mTess.init(datapath, "eng");
            String result = GetOCRResult(mTess, receivedBitmap);
            Log.d("result", "result: " + result);
            text.setText(result);
            GetCode(result);
        } else {
            Log.d("SetImage", "no image");
        }

        TextView cameratext = findViewById(R.id.CameraText);
        cameratext.setText("Use below button to validation code.");

    }

    public void cameera(View v) {

        Intent intent = new Intent(VerificationCode_Camera.this, Camera.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);

    }

    private void StartTess() {
        try {
            File dir = getExternalFilesDir(tessdata);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    Log.d("Create File", "Create File Faile: " + dir.getPath());
                }
            }
            String pathtodataFile = dir + "/" + fileNameLang;
            if (!(new File(pathtodataFile)).exists()) {
                AssetManager assetManager = getAssets();
                InputStream instream = assetManager.open(fileNameLang);
                OutputStream outstream = new FileOutputStream(pathtodataFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, read);
                }
//                    outstream.flush();
                outstream.close();
                instream.close();
            }
        } catch (Exception e) {
            Log.d("StartTess", "read file faile: " + e.getMessage());
//            e.printStackTrace();
        }
    }

    private String GetOCRResult(TessBaseAPI mTess, Bitmap bitmap) {
        mTess.setImage(bitmap);
        String result = "";
        try {
            result = mTess.getUTF8Text();
        } catch (Exception e) {
            e.getMessage();
        }
        mTess.end();
        Log.d("OCRResult", "result：" + result);
        return result;
    }


    public void Back(View view) {
        Intent intent = new Intent(VerificationCode_Camera.this, StaffHome.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }

    public void GetCode(String result) {
        DatabaseReference CodeCheckDatabase = FirebaseDatabase.getInstance().getReference("CodeCheck");

        CodeCheckDatabase.orderByChild("code")
                .equalTo(result).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("CodeCheck_database", "Error getting CodeCheck detail!");
                            Log.d("Verify code", "Ticket code Invalid!");
                            Toast.makeText(VerificationCode_Camera.this, "Ticket code Invalid!", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DataSnapshot CodeCheckSnapshot : dataSnapshot.getChildren()) {
                                VerificationCode codeCheck = CodeCheckSnapshot.child("").getValue(VerificationCode.class);
                                System.out.println("CodeCheckSnapshot");
                                System.out.println(codeCheck);
                                float timeID = codeCheck.getTimeId();

                                GetTimeSlotDetail(timeID);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });

    }

    private void GetTimeSlotDetail(float timeID) {
        DatabaseReference TimeSlotDatabase = FirebaseDatabase.getInstance().getReference("TimeSlot");

        TimeSlotDatabase.orderByChild("TimeId")
                .equalTo(timeID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("TimeSlot_database", "Error getting TimeSlot detail!");
                        } else {
                            for (DataSnapshot TimeSlotSnapshot : dataSnapshot.getChildren()) {
                                Slot timeslot = TimeSlotSnapshot.child("").getValue(Slot.class);
                                System.out.println("TimeSlotSnapshot");
                                System.out.println(timeslot);

                                //check time valid
                                LocalTime localTime = LocalTime.now();
                                LocalTime Start_Time = LocalTime.of(Integer.valueOf(timeslot.getTime_Start()),0);
                                LocalTime End_Time = LocalTime.of(Integer.valueOf(timeslot.getTime_End()),0);
                                if (localTime.isAfter(Start_Time) && localTime.isBefore(End_Time)) {
                                    Toast.makeText(VerificationCode_Camera.this, "Ticket Valid", Toast.LENGTH_SHORT).show();
                                    Log.d("VerifyCodeTime", "Ticket code Valid!");
                                } else {
                                    Toast.makeText(VerificationCode_Camera.this, "Ticket Invalid, not in timeslot", Toast.LENGTH_SHORT).show();
                                    Log.d("VerifyCodeTime", "Ticket code Valid, but not in timeslot!");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });
    }


}