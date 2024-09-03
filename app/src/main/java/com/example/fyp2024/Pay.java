package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.fyp2024.DB.NotificationReceiver;
import com.example.fyp2024.DB.NotificationReceiverForPayment;
import com.example.fyp2024.DB.Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Pay extends AppCompatActivity {
    String name;
    String email;
    String userID;
    String total_price;
    String total_amount_of_people;

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
//    public static final String TOTAL_PRICE = "com.example.fyp2024.TOTAL_PRICE";
//    public static final String TOTAL_PEOPLE = "com.example.fyp2024.TOTAL_PEOPLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(BuyTicketConfirm.USERID);
            name = intent.getStringExtra(BuyTicketConfirm.USER_NAME);
            email = intent.getStringExtra(BuyTicketConfirm.USER_EMAIL);
            email = intent.getStringExtra(BuyTicketConfirm.USER_EMAIL);
            email = intent.getStringExtra(BuyTicketConfirm.USER_EMAIL);

            total_price = intent.getStringExtra(BuyTicketConfirm.TOTAL_PRICE);
            total_amount_of_people = intent.getStringExtra(BuyTicketConfirm.TOTAL_PEOPLE);
            Log.d("Pay Page", "Pay Page");


//            TextView EventSelected = findViewById(R.id.EventSelected);
//            EventSelected.setText(event_selected);
        } else {
            Toast.makeText(Pay.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Pay(View view){
        Log.d("Pay Page", "Pay func");

            DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
            paymentDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        long Count_record = dataSnapshot.getChildrenCount();
                        Log.d("Pay Page", "Count_record!" + Count_record);

                        if (Count_record == 0) {
                            long paymentId = 0;
//                            SavePaymentId(paymentId);
                            Payment payment = new Payment(paymentId, total_amount_of_people, total_price, Long.valueOf(userID),"Y");
                            paymentDatabase.child("Payment" + paymentId).setValue(payment);
                            Log.d("Pay Page", "paymentId set up!");
                            Log.d("Pay Page", "paymentId!"+paymentId);

//                            Booking_Confirm();

                        } else {
                            paymentDatabase.orderByChild("paymentId")
                                    .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()) {
                                                Log.d("Pay Page", "Error getting Payment detail!");
                                            } else {
                                                for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                                                    Object paymentId_object = paymentSnapshot.child("paymentId").getValue();
                                                    String paymentId_rec = String.valueOf(paymentId_object);
                                                    System.out.println("paymentId_rec " + paymentId_rec);
                                                    long paymentId_long = Long.parseLong(paymentId_rec);

                                                    long new_paymentId_long = paymentId_long + 1;
                                                    Log.d("Pay Page", "new_paymentId_long!"+new_paymentId_long);
//                                                    SavePaymentId(new_paymentId_long);

                                                    Payment payment = new Payment(new_paymentId_long, total_amount_of_people, total_price, Long.valueOf(userID),"Y");
                                                    paymentDatabase.child("Payment" + new_paymentId_long).setValue(payment);
                                                    Log.d("Pay Page", "payment set up!");

//                                                    Booking_Confirm();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.err.println("Error getting data: " + databaseError.getMessage());
                                        }
                                    });
                        }
                    } else {
                        Log.e("Pay Page", "Error getting data", task.getException());
                    }
                }
            });

        Toast toast = Toast.makeText(Pay.this, "You have make payment! Thankyou!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


        PaymentNotification();
//        Toast.makeText(Pay.this, "You have make payment! Thankyou!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Pay.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

//    public void BackBtn(View view){
//        Intent intent = new Intent(Pay.this, ViewBooking.class);
//        intent.putExtra(USERID, userID);
//        intent.putExtra(USER_NAME, name);
//        intent.putExtra(USER_EMAIL, email);
//        startActivity(intent);
//    }

    public void PaymentNotification(){

        Intent intent = new Intent(this, NotificationReceiverForPayment.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE,0);
        Log.d("PaymentNotification","Payment will delete at Time "+calendar.getTime());

        int userID_int_and_requestcode = Integer.parseInt(userID);

        intent.putExtra("user_id", userID_int_and_requestcode);
        Log.d("PaymentNotification","userID_int_and_requestcode: "+userID_int_and_requestcode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, userID_int_and_requestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("PaymentNotification","Has permission");
            } catch (SecurityException e) {
                Log.d("PaymentNotification","No permission");
            }
        }

        //normal payment notification
        String channelId = "payment_channel_id";
        String channelName = "Payment_Channel";

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            Log.d("PaymentNotification","payment channel created");

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Thank for buy ticket!")
                .setContentText("You have buy a ticket.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have buy a ticket."+"Ticket will delete at Time "+calendar.getTime()+", Enjoy!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);


        notificationManager.notify(100, builder.build());
        Log.d("PaymentNotification","display,at pay page");

    }


}