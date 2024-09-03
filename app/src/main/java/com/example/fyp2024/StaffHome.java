package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StaffHome extends AppCompatActivity {
    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";
    String name;
    String email;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        TextView user_text= findViewById(R.id.Staff_text);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(StaffLogin.STAFF_USERID);
            name = intent.getStringExtra(StaffLogin.STAFF_USER_NAME);
            email = intent.getStringExtra(StaffLogin.STAFF_USER_EMAIL);
            user_text.setText("Hi, "+name+"!");
//            Toast.makeText(Home.this, "Id!"+userID, Toast.LENGTH_SHORT).show();
        }

    }

    public void GotoUpdate(View view){
//        Intent intent = new Intent(StaffHome.this, UpdateUser.class);
//        intent.putExtra(USERID, userID);
//        intent.putExtra(USER_NAME, name);
//        intent.putExtra(USER_EMAIL, email);
//        startActivity(intent);
    }

    public void Logout(View view){
        Intent intent = new Intent(StaffHome.this, MainActivity.class);
        startActivity(intent);
    }

    public void View_Booking(View view){
        Intent intent = new Intent(StaffHome.this, ViewBooking_Staff.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }

    public void VerifyCode(View view){
        Intent intent = new Intent(StaffHome.this, VerificationCode_Camera.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }

    public void Verify_QR_Code(View view){
        Intent intent = new Intent(StaffHome.this, Scan_QR_Code_Activity.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }

}