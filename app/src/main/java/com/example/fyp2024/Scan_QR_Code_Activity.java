package com.example.fyp2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fyp2024.DB.Slot;
import com.example.fyp2024.DB.VerificationCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.time.LocalTime;

public class Scan_QR_Code_Activity extends AppCompatActivity {
    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";
    String name;
    String email;
    String userID;

    private TextView qrCodeResult;
    private TextView validation_checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qr_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(StaffHome.STAFF_USERID);
            name = intent.getStringExtra(StaffHome.STAFF_USER_NAME);
            email = intent.getStringExtra(StaffHome.STAFF_USER_EMAIL);
        }

        qrCodeResult = findViewById(R.id.QR_Code_Result);
        validation_checker = findViewById(R.id.Validation_checker);

        Button scanButton = findViewById(R.id.Scan_QR_Btn);

        qrCodeResult.setText("Use below button to Scan and verification QR code.");

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Scan_QR_Code_Activity.this).initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                qrCodeResult.setText("QR Code Result: " + result.getContents());

                Verifier_QR_Code_Result(result.getContents());
            } else {
                qrCodeResult.setText("Scan canceled because no value or error.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void Scan_QR_Code_BackBtn(View v){
        Intent intent = new Intent(Scan_QR_Code_Activity.this, StaffHome.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }


    public void Verifier_QR_Code_Result(String result) {
        DatabaseReference CodeCheckDatabase = FirebaseDatabase.getInstance().getReference("CodeCheck");

        CodeCheckDatabase.orderByChild("code")
                .equalTo(result).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("CodeCheck_database", "Error getting CodeCheck detail!");
                            Log.d("Verify code", "Ticket code Invalid!");
                            validation_checker.setText("Ticket code Invalid!");
                            Toast.makeText(Scan_QR_Code_Activity.this, "Ticket code Invalid!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Scan_QR_Code_Activity.this, "Ticket Valid", Toast.LENGTH_SHORT).show();
                                    Log.d("VerifyCodeTime", "Ticket code Valid!");
                                    validation_checker.setText("Ticket Valid!");

                                } else {
                                    validation_checker.setText("Ticket Invalid, not in timeslot!");
                                    Toast.makeText(Scan_QR_Code_Activity.this, "Ticket Invalid, not in timeslot", Toast.LENGTH_SHORT).show();
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