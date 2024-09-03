package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.NotificationReceiver;
import com.example.fyp2024.DB.NotificationReceiverForPayment;

import java.util.Calendar;

public class BuyTicketConfirm extends AppCompatActivity {

    String name;
    String email;
    String userID;
    String ticket_detail;
    String total_price;
    String total_amount_of_people;

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String TOTAL_PRICE = "com.example.fyp2024.TOTAL_PRICE";
    public static final String TOTAL_PEOPLE = "com.example.fyp2024.TOTAL_PEOPLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket_confirm);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(BuyTicket.USERID);
            name = intent.getStringExtra(BuyTicket.USER_NAME);
            email = intent.getStringExtra(BuyTicket.USER_EMAIL);
//            event_selected = intent.getStringExtra(BookEvent.EVENT_SELECTED);
            total_price = intent.getStringExtra(BuyTicket.TOTAL_PRICE);
            total_amount_of_people = intent.getStringExtra(BuyTicket.TOTAL_PEOPLE);
            ticket_detail = intent.getStringExtra(BuyTicket.TICKET_DETAIL);

            Log.d("Bookconfirm page", "Bookconfirm here check");
//            Log.d("Bookconfirm page", "bookingID"+bookingID);
//            Log.d("Bookconfirm page", "paymentID"+paymentID);

            TextView EventSelected = findViewById(R.id.EventSelected);
                        Log.d("ticket page", "ticket_detail"+ticket_detail);

            EventSelected.setText("You are currently buying ticket"+ "\n" + ticket_detail);
        } else {
            Toast.makeText(BuyTicketConfirm.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

    }

    public void PayNow(View view){
        Intent intent = new Intent(BuyTicketConfirm.this, Pay.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        intent.putExtra(TOTAL_PRICE, total_price);
        intent.putExtra(TOTAL_PEOPLE, total_amount_of_people);
        startActivity(intent);
    }

    public void Back_to_Home(View view){
        Intent intent = new Intent(BuyTicketConfirm.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }




}