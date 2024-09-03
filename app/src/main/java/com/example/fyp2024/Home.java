package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.Payment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Home extends AppCompatActivity {

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String TICKET_LIMIT = "com.example.fyp2024.TICKET_LIMIT";
    String name;
    String email;
    String userID;
    String ticket_limit;
    boolean has_ticket = false;
    TextView user_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user_text= findViewById(R.id.User_text);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(Login.USERID);
            name = intent.getStringExtra(Login.USER_NAME);
            email = intent.getStringExtra(Login.USER_EMAIL);
            user_text.setText("Hi, "+name+"! Get a ticket to book rides!");
//            Toast.makeText(Home.this, "Id!"+userID, Toast.LENGTH_SHORT).show();
            getPaymentTicketValid();
            //set time ticket distory********************
        }


    }

    public void BuyTicket(View view){
        Intent intent = new Intent(Home.this, BuyTicket.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

    public void GotoUpdate(View view){
        Intent intent = new Intent(Home.this, UpdateUser.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

    public void Logout(View view){
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
    }

    public void View_Booking(View view){
        Intent intent = new Intent(Home.this, ViewBooking.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

    public void Go_to_Booking(View view){
        Intent intent = new Intent(Home.this, Facilities.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        intent.putExtra(TICKET_LIMIT, ticket_limit);
        startActivity(intent);
    }

    public void Map_Location(View view){
        Intent intent = new Intent(Home.this, Map_Location_Activity.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

    public void getPaymentTicketValid(){//delete ticket
        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");

        Log.d("Payment", "Data getting " + userID);

        paymentDatabase.orderByChild("userId")
                .equalTo(Long.valueOf(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("payment_database", "Error getting payment detail!");
                            Log.d("Home ticket", "no ticket");
                            Button BookEventBtn = findViewById(R.id.Go_to_BookBtn);
                            BookEventBtn.setFocusable(false);
                            BookEventBtn.setClickable(false);
                            BookEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));

                            user_text.setText("Hi, "+name+"! Get a ticket to book rides!");
                        } else {
                            for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                                Payment payment = paymentSnapshot.child("").getValue(Payment.class);
                                System.out.println("paymentSnapshot");
                                System.out.println(payment);

                                if(payment.getPayment().equals("Y")){
                                    has_ticket = true;
                                    ticket_limit = payment.getPeople_amount();
                                    Log.d("ticket_limit","ticket_limit:" + ticket_limit);

                                    Button BuyTicketBtn = findViewById(R.id.TicketBtn);
                                    BuyTicketBtn.setFocusable(false);
                                    BuyTicketBtn.setClickable(false);
                                    BuyTicketBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));

                                    user_text.setText("Hi, "+name+"! You got a ticket, enjoy your travel!");
                                }else{
                                    Log.d("Home ticket", "no ticket");
                                    Button BookEventBtn = findViewById(R.id.Go_to_BookBtn);
                                    BookEventBtn.setFocusable(false);
                                    BookEventBtn.setClickable(false);
                                    BookEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));

                                    user_text.setText("Hi, "+name+"! Get a ticket to book rides!");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });



//        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
//        LocalTime localTime = LocalTime.now();
//        LocalTime Start_Time = LocalTime.of(Integer.valueOf(22),0);
//        LocalTime End_Time = LocalTime.of(Integer.valueOf(23),0);
//        if (localTime.isAfter(Start_Time) && localTime.isBefore(End_Time)) {
//            Log.d("Payment", "Data delete " + userID);
//
//            paymentDatabase.orderByChild("userId")
//                    .equalTo(Long.valueOf(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        snapshot.getRef().removeValue()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d("Payment", "Data with userId " + userID + " deleted successfully");
//                                        Toast.makeText(Home.this, "Expired Ticket has been delete.", Toast.LENGTH_SHORT).show();
//                                        user_text.setText("Hi, "+name+"! Get a ticket to book events!");
//
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e("Payment", "Error deleting data with userId " + userID + ": " + e.getMessage());
//                                    }
//                                });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e("CodeCheck", "Error querying data: " + databaseError.getMessage());
//                }
//            });
//        } else {
//            Log.d("Payment", "Data getting " + userID);
//
//            paymentDatabase.orderByChild("userId")
//                    .equalTo(Long.valueOf(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (!dataSnapshot.exists()) {
//                                Log.d("payment_database", "Error getting payment detail!");
//                                Log.d("Home ticket", "no ticket");
//                                Button BookEventBtn = findViewById(R.id.Go_to_BookBtn);
//                                BookEventBtn.setFocusable(false);
//                                BookEventBtn.setClickable(false);
//                                BookEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//                                user_text.setText("Hi, "+name+"! Get a ticket to book events!");
//                            } else {
//                                for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
//                                    Payment payment = paymentSnapshot.child("").getValue(Payment.class);
//                                    System.out.println("paymentSnapshot");
//                                    System.out.println(payment);
//
//                                    if(payment.getPayment().equals("Y")){
//                                        has_ticket = true;
//                                        ticket_limit = payment.getPeople_amount();
//                                        Log.d("ticket_limit","ticket_limit:" + ticket_limit);
//
//                                        Button BuyTicketBtn = findViewById(R.id.TicketBtn);
//                                        BuyTicketBtn.setFocusable(false);
//                                        BuyTicketBtn.setClickable(false);
//                                        BuyTicketBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//                                        user_text.setText("Hi, "+name+"! You got an ticket, enjoy!");
//                                    }else{
//                                        Log.d("Home ticket", "no ticket");
//                                        Button BookEventBtn = findViewById(R.id.Go_to_BookBtn);
//                                        BookEventBtn.setFocusable(false);
//                                        BookEventBtn.setClickable(false);
//                                        BookEventBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//                                        user_text.setText("Hi, "+name+"! Get a ticket to book events!");
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            System.err.println("Error getting data: " + databaseError.getMessage());
//                        }
//                    });
//        }

    }


}