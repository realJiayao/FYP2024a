package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.NotificationReceiver;
import com.example.fyp2024.DB.UserBooking;
import com.example.fyp2024.DB.VerificationCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Calendar;

public class BookEvent extends AppCompatActivity {

    String name;
    String email;
    String userID;
    String facilityID;
    String facilityName;
    String timeId;
    String timeSlot;
    String verification_code;
    String total_price;
    String total_amount_of_people;
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String FACILITY_ID = "com.example.fyp2024.FACILITY_ID";
    public static final String FACILITY_NAME = "com.example.fyp2024.FACILITY_NAME";
    public static final String TIME_ID = "com.example.fyp2024.TIME_ID";
    public static final String TIMESLOT = "com.example.fyp2024.TIMESLOT";
    public static final String TOTAL_PRICE = "com.example.fyp2024.TOTAL_PRICE";
    public static final String TOTAL_PEOPLE = "com.example.fyp2024.TOTAL_PEOPLE";
    public static final String BOOKING_ID = "com.example.fyp2024.BOOKING_ID";
    public static final String PAYMENT_ID = "com.example.fyp2024.PAYMENT_ID";
    public static final String EVENT_SELECTED = "com.example.fyp2024.EVENT_SELECTED";


    long codeId_record;
    long bookingId_record;
    long paymentId_record;
    String event_selected;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_event);


        Log.d("BookEvent Page", "BookEvent Page");

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(BookingAmount.USERID);
            name = intent.getStringExtra(BookingAmount.USER_NAME);
            email = intent.getStringExtra(BookingAmount.USER_EMAIL);
            facilityID = intent.getStringExtra(BookingAmount.FACILITY_ID);
            facilityName = intent.getStringExtra(BookingAmount.FACILITY_NAME);
            timeId = intent.getStringExtra(BookingAmount.TIME_ID);
            timeSlot = intent.getStringExtra(BookingAmount.TIMESLOT);
//            total_price = intent.getStringExtra(BookingAmount.TOTAL_PRICE);
            total_amount_of_people = intent.getStringExtra(BookingAmount.TOTAL_PEOPLE);

            TextView EventSelected = findViewById(R.id.EventSelected);
            event_selected = facilityName + " (" + timeSlot + ")"+ "\n" +
                    "Total people: "+total_amount_of_people;

            EventSelected.setText("You are currently booking: " + event_selected);
        } else {
            Toast.makeText(BookEvent.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }
    }

    public void BookEvent(View view) {

        Log.d("BookEvent Page", "BookEvent");
        //change 2024 to now time:20:38 eg

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        verification_code = userID + facilityID + timeId + hour + minute + second;
        UpdateCurrentAttend();
        AddVerification_code();

        DatabaseReference userBookingDatabase = FirebaseDatabase.getInstance().getReference("UserBooking");
        userBookingDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    long Count_record = dataSnapshot.getChildrenCount();
                    Log.d("BookEvent Page", "Count_record!" + Count_record);


                    if (Count_record == 0) {
                        long bookingId = 0;
                        SaveBookingId(bookingId);
                        Log.d("BookEvent Page", "codeId_record!"+codeId_record);
                        UserBooking userbooking = new UserBooking(bookingId, Long.parseLong(userID),
                                Long.parseLong(facilityID),
                                Long.parseLong(timeId),total_amount_of_people, verification_code,facilityName,timeSlot,codeId_record);
                        System.out.println("userbooking");
                        System.out.println(userbooking);
                        userBookingDatabase.child("UserBooking" + bookingId).setValue(userbooking);
                        Log.d("BookEvent Page", "userbooking set up!");

                        SetAlarmMessage(BookEvent.this);

                    } else {
                        Log.d("BookEvent Page", "case:booking not null!");

                        userBookingDatabase.orderByChild("bookingId")
                                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Log.d("BookEvent Page", "Error getting userBooking detail!");
                                        } else {
                                            for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                                                Log.d("BookEvent Page", "booking not null process!");

                                                Object bookingId_object = bookingSnapshot.child("bookingId").getValue();
                                                String bookingId_rec = String.valueOf(bookingId_object);
                                                System.out.println("BookingId_rec " + bookingId_rec);
                                                long BookingIdlong = Long.parseLong(bookingId_rec);

                                                long new_BookingIdlong = BookingIdlong + 1;
                                                SaveBookingId(new_BookingIdlong);

                                                Log.d("BookEvent Page", "booking not null verification_code process!");

                                                Log.d("BookEvent Page", "codeId_record!"+codeId_record);
                                                UserBooking userbooking = new UserBooking(new_BookingIdlong, Long.parseLong(userID),
                                                        Long.parseLong(facilityID),
                                                        Long.parseLong(timeId),total_amount_of_people, verification_code,facilityName,timeSlot,codeId_record);
                                                System.out.println("userbooking");
                                                System.out.println(userbooking);
                                                userBookingDatabase.child("UserBooking" + new_BookingIdlong).setValue(userbooking);
                                                Log.d("BookEvent Page", "userbooking set up!");


                                                SetAlarmMessage(BookEvent.this);

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
                    Log.e("BookEvent Page", "Error getting data", task.getException());
                }
            }
        });

        Booking_Confirm();
//        AddPayment();

    }

    public void NoBtn_back(View view) {
        Intent intent = new Intent(BookEvent.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
//        intent.putExtra(FACILITY_ID, facilityID);
//        intent.putExtra(FACILITY_NAME, facilityName);
        startActivity(intent);
    }

    public void AddVerification_code() {
        DatabaseReference codeCheckDatabase = FirebaseDatabase.getInstance().getReference("CodeCheck");
        codeCheckDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    long Count_record = dataSnapshot.getChildrenCount();
                    Log.d("BookEvent Page, Ver Code", "Count_record!" + Count_record);

                    if (Count_record == 0) {
                        long codeCheckId = 0;
                        //change 2024 to now time:20:38 eg
                        VerificationCode verificationCode = new VerificationCode(codeCheckId, verification_code, Long.valueOf(timeId));
                        codeCheckDatabase.child("CodeCheck" + codeCheckId).setValue(verificationCode);
                        Log.d("BookEvent Page", "codeCheck set up!");
                        Log.d("BookEvent Page", "codeCheckId!"+codeCheckId);


                        SaveCodeId(codeCheckId);

                    } else {
                        codeCheckDatabase.orderByChild("codeId")
                                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Log.d("BookEvent Page", "Error getting CodeCheck detail!");
                                        } else {
                                            for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                                                Object codeCheckId_object = bookingSnapshot.child("codeId").getValue();
                                                String codeCheckId_rec = String.valueOf(codeCheckId_object);
                                                System.out.println("codeCheckId_rec " + codeCheckId_rec);
                                                long codeCheckId_long = Long.parseLong(codeCheckId_rec);

                                                long new_codeCheckId_long = codeCheckId_long + 1;
                                                Log.d("BookEvent Page", "new_codeCheckId_long!"+new_codeCheckId_long);


                                                VerificationCode verificationCode = new VerificationCode(new_codeCheckId_long, verification_code, Long.valueOf(timeId));
                                                codeCheckDatabase.child("CodeCheck" + new_codeCheckId_long).setValue(verificationCode);
                                                Log.d("BookEvent Page", "CodeCheck set up!");
                                                SaveCodeId(new_codeCheckId_long);
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
                    Log.e("BookEvent Page", "Error getting data", task.getException());
                }
            }
        });
    }

    private void SaveCodeId(long codeCheckId) {
        codeId_record = codeCheckId;
        Log.d("BookEvent Page", "saved codeId_record!"+codeCheckId);

    }
    private void SaveBookingId(long bookingId) {
        bookingId_record = bookingId;
        Log.d("BookEvent Page", "saved bookingId_record!"+bookingId);

    }
//    private void SavePaymentId(long paymentId) {
//        paymentId_record = paymentId;
//        Log.d("BookEvent Page", "saved paymentId_record!"+paymentId);
//
//    }


    public void UpdateCurrentAttend() {
        Log.d("BookEvent Page", "UpdateCurrentAttend func!"+timeId);

        DatabaseReference timeSlotDatabase = FirebaseDatabase.getInstance().getReference("TimeSlot");
        timeSlotDatabase.orderByChild("TimeId")
                .equalTo(Long.valueOf(timeId)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("BookEvent Page", "Error getting timeSlot detail!");
                        } else {
                            for (DataSnapshot timeSlotSnapshot : dataSnapshot.getChildren()) {
                                String currentAttend_str = (String) timeSlotSnapshot.child("Current_Attend").getValue();
                                int currentAttend = Integer.valueOf(currentAttend_str);
//                                currentAttend += Integer.valueOf(total_amount_of_people);//need to change
                                float total_people =  Float.valueOf(total_amount_of_people);

                                Log.d("BookEvent Page", "total_people: "+total_people);
                                currentAttend = currentAttend+(int)total_people;
                                timeSlotDatabase.child("Time" + timeId).child("Current_Attend").setValue(String.valueOf(currentAttend));
                                Log.d("BookEvent Page", "current attend update!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });
    }

//    public void AddPayment() {
//        Log.d("BookEvent Page", "payment!");
//
//        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
//        paymentDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DataSnapshot dataSnapshot = task.getResult();
//                    long Count_record = dataSnapshot.getChildrenCount();
//                    Log.d("BookEvent Page", "Count_record!" + Count_record);
//
//                    if (Count_record == 0) {
//                        long paymentId = 0;
//                        SavePaymentId(paymentId);
//                        Payment payment = new Payment(paymentId, total_amount_of_people, total_price, bookingId_record,"N");
//                        paymentDatabase.child("Payment" + paymentId).setValue(payment);
//                        Log.d("BookEvent Page", "paymentId set up!");
//                        Log.d("BookEvent Page", "paymentId!"+paymentId);
//
//                        Booking_Confirm();
//
//                    } else {
//                        paymentDatabase.orderByChild("paymentId")
//                                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (!dataSnapshot.exists()) {
//                                            Log.d("BookEvent Page", "Error getting Payment detail!");
//                                        } else {
//                                            for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
//                                                Object paymentId_object = paymentSnapshot.child("paymentId").getValue();
//                                                String paymentId_rec = String.valueOf(paymentId_object);
//                                                System.out.println("paymentId_rec " + paymentId_rec);
//                                                long paymentId_long = Long.parseLong(paymentId_rec);
//
//                                                long new_paymentId_long = paymentId_long + 1;
//                                                Log.d("BookEvent Page", "new_paymentId_long!"+new_paymentId_long);
//                                                SavePaymentId(new_paymentId_long);
//
//                                                Payment payment = new Payment(new_paymentId_long, total_amount_of_people, total_price, bookingId_record,"N");
//                                                paymentDatabase.child("Payment" + new_paymentId_long).setValue(payment);
//                                                Log.d("BookEvent Page", "payment set up!");
//
//                                                Booking_Confirm();
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        System.err.println("Error getting data: " + databaseError.getMessage());
//                                    }
//                                });
//                    }
//                } else {
//                    Log.e("BookEvent Page", "Error getting data", task.getException());
//                }
//            }
//        });
//    }




    public void Booking_Confirm(){
        Toast.makeText(BookEvent.this, "Event booked!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(BookEvent.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
//        Log.d("Bookevent","bookingId_record"+bookingId_record);
//        Log.d("Bookevent","paymentId_record"+paymentId_record);
//
//        Log.d("Bookevent","event_selected:"+event_selected);

//        intent.putExtra(EVENT_SELECTED, event_selected);
//        intent.putExtra(BOOKING_ID, String.valueOf(bookingId_record));
//        intent.putExtra(PAYMENT_ID, String.valueOf(paymentId_record));
        startActivity(intent);
    }


    public void SetAlarmMessage(Context content){
        //timing booking notification
        Intent intent = new Intent(this, NotificationReceiver.class);
        Log.d("AlarmMessage","AlarmMessage SetUP");

        String timeSlot_split = timeSlot.replaceAll("\\s", "");
        String[] timeSlot_split_array = timeSlot_split.split("-");

        String Start_Time_array = timeSlot_split_array[0];
        String[] Start_Time_hour = Start_Time_array.split(":");
        String Start_hour = Start_Time_hour[0];

        int Start_hour_int = Integer.parseInt(Start_hour);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Start_hour_int);
        calendar.set(Calendar.MINUTE,0);
        calendar.add(Calendar.MINUTE, -5);
        Log.d("AlarmMessage","AlarmMessage Time "+Start_hour_int);

        String bookingId_record_String = String.valueOf(bookingId_record);
        int bookingId_record_int_request_code = Integer.parseInt(bookingId_record_String);

        intent.putExtra("notification_message", timeSlot);
        intent.putExtra("notification_id", bookingId_record_int_request_code);
        intent.putExtra("notification_facilityName", facilityName);
        Log.d("AlarmMessage","bookingId_record_int_request_code: "+bookingId_record_int_request_code);

        //requestId is same to notificationID, Extra content saved on database.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, bookingId_record_int_request_code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("alarmManager","Has permission");
            } catch (SecurityException e) {
                Log.d("alarmManager","No permission");
            }
        }

        //normal notification for booking
        String channelId = "booking_channel_id";
        String channelName = "Booking_Channel";

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        }

//        String normal_notification_booking = "You have book "+facilityName +" at "+timeSlot+".";


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Thank for Booking!")
                .setContentText("You have book "+facilityName +" at "+timeSlot+".")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have book "+facilityName +" at "+timeSlot+"."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

//        Notification notification = new Notification.Builder(this, channelId)
//                .setContentTitle("Thank for Booking!")
//                .setContentText("You have booked "+facilityName +" at "+timeSlot+".")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .build();

        notificationManager.notify(100, builder.build());
        Log.d("Normal notification","display,at booking even page");



    }

}