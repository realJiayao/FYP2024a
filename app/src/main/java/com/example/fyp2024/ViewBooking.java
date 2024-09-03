package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.NotificationReceiver;
import com.example.fyp2024.DB.Slot;
import com.example.fyp2024.DB.UserBooking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.Calendar;

public class ViewBooking extends AppCompatActivity {
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String BOOKING_ID = "com.example.fyp2024.BOOKING_ID";
    public static final String PAYMENT_ID = "com.example.fyp2024.PAYMENT_ID";
    public static final String USER_VERIFICATIONCODE = "com.example.fyp2024.USER_VERIFICATIONCODE";

    String name;
    String email;
    String userID;
//    ArrayList<UserBooking> userBookings = new ArrayList<>();

//    DatabaseReference timeslotDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(TimeSlot.USERID);
            name = intent.getStringExtra(TimeSlot.USER_NAME);
            email = intent.getStringExtra(TimeSlot.USER_EMAIL);
            Log.d("message", "userIDl!" + userID);

        } else {
            Toast.makeText(ViewBooking.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

        GetBooking();

    }

    public void ViewBooking_BackBtn(View view) {
        Intent intent = new Intent(ViewBooking.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

    public void GetBooking() {
        DatabaseReference userBookingDatabase = FirebaseDatabase.getInstance().getReference("UserBooking");

        userBookingDatabase.orderByChild("userId")
                .equalTo(Long.valueOf(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("message", "Error getting userBooking detail!");
                        } else {
                            for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                                UserBooking userBooking = bookingSnapshot.child("").getValue(UserBooking.class);
                                System.out.println("bookingSnapshot");
                                System.out.println(userBooking);


                                SetBookingDetail(userBooking);

                                //get payment
//                                GetPayment(userBooking);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });
    }

//    public void GetPayment(UserBooking userBooking){
//        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
//
//        paymentDatabase.orderByChild("bookingId")
//                .equalTo(Long.valueOf(userBooking.getBookingId())).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (!dataSnapshot.exists()) {
//                            Log.d("payment_database", "Error getting payment detail!");
//                        } else {
//                            for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
//                                Payment payment = paymentSnapshot.child("").getValue(Payment.class);
//                                System.out.println("paymentSnapshot");
//                                System.out.println(payment);
//
//                                SetBookingDetail(userBooking,payment);
//                                //get payment
////                                SetBookingDetail(userBooking);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        System.err.println("Error getting data: " + databaseError.getMessage());
//                    }
//                });
//
//
//    }

    public void SetBookingDetail(UserBooking userBooking) {

        System.out.println("setting detail");
        TableLayout tableLayout = findViewById(R.id.tableLayout_viewBooking);
        TableRow tableRow = new TableRow(this);

        TextView textView = new TextView(this);
        textView.setText(userBooking.getFacilityName());
        textView.setPadding(8, 8, 8, 8);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView);

        System.out.println(userBooking.getFacilityName());

        TextView textView1 = new TextView(this);
        textView1.setText(userBooking.getTimeSlot());
        textView1.setPadding(8, 8, 8, 8);
        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView1);
        System.out.println(userBooking.getTimeSlot());

        TextView textView2 = new TextView(this);
        textView2.setText(userBooking.getVerification_code());
        textView2.setPadding(8, 8, 8, 8);
        textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView2);
        System.out.println(userBooking.getVerification_code());

        TextView textView3 = new TextView(this);
        textView3.setText(userBooking.getBooking_amount());
        textView3.setPadding(8, 8, 8, 8);
        textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView3);
        System.out.println(userBooking.getBooking_amount());

        //2024 08 redo
        Button QR_code_buttonTextView = new Button(this);
        QR_code_buttonTextView.setText("QR Code");
        QR_code_buttonTextView.setPadding(8, 8, 8, 8);
        QR_code_buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QR_code_handleButtonClick(userBooking.getVerification_code());
            }
        });

        tableRow.addView(QR_code_buttonTextView);


        //payment
//        Log.d("ViewBooking Pay","payid:"+payment.getPaymentId());
//
//        float paymentID = payment.getPaymentId();
//        Log.d("paymentID","paymentID:"+paymentID);
//
//        int paymentID_int = (int)paymentID;
//        Log.d("paymentID_int","paymentID_int:"+paymentID_int);
//
//
//        TextView textView3 = new TextView(this);
//        textView3.setText(payment.getPayment());
//        textView3.setPadding(8, 8, 8, 8);
//        textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        tableRow.addView(textView3);
//        System.out.println(payment.getPayment());
////
//        TextView textView4 = new TextView(this);
//        String people_amount = payment.getPeople_amount();
//        float people_amount_float = Float.parseFloat(people_amount);
//        Log.d("people_amount_float","people_amount_float:"+people_amount_float);
//
//        int people_amount_int = (int)people_amount_float;
//        Log.d("people_amount_int","people_amount_int:"+people_amount_int);
//
//        textView4.setText(""+people_amount_int);
//        textView4.setPadding(8, 8, 8, 8);
//        textView4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        tableRow.addView(textView4);
//        System.out.println(payment.getPeople_amount());
//
//        TextView textView5 = new TextView(this);
//        textView5.setText(payment.getPrice());
//        textView5.setPadding(8, 8, 8, 8);
//        textView5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        tableRow.addView(textView5);
//        System.out.println(payment.getPrice());
//
//        Button pay_buttonTextView = new Button(this);
//        pay_buttonTextView.setText("Pay");
//        pay_buttonTextView.setPadding(8, 8, 8, 8);
//        pay_buttonTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                handleButtonClick(userBooking.getBookingId(), userBooking.getTimeId(), userBooking.getCodeCheckId());
//                Intent intent = new Intent(ViewBooking.this, Pay.class);
//                intent.putExtra(USERID, userID);
//                intent.putExtra(USER_NAME, name);
//                intent.putExtra(USER_EMAIL, email);
//                intent.putExtra(BOOKING_ID, String.valueOf(userBooking.getBookingId()));
//                intent.putExtra(PAYMENT_ID, String.valueOf(paymentID_int));
//
//                startActivity(intent);
//            }
//        });
//
//        if(payment.getPayment().equals("Y")){//disable button if payment = yes
//            pay_buttonTextView.setFocusable(false);
//            pay_buttonTextView.setClickable(false);
////            pay_buttonTextView.setBackgroundColor();
//
//        }else{
////            pay_buttonTextView.setBackgroundColor(Color.parseColor("#A8D3E9"));
//            pay_buttonTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//        }

        Button cancel_buttonTextView = new Button(this);
        cancel_buttonTextView.setText("Cancel");
        cancel_buttonTextView.setPadding(8, 8, 8, 8);
        cancel_buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(userBooking.getBookingId(), userBooking.getTimeId(), userBooking.getCodeCheckId(), userBooking.getBooking_amount());
                RemoveNotification(userBooking.getBookingId(), userBooking.getFacilityName(), userBooking.getTimeSlot());
            }
        });

//        LocalTime localTime = LocalTime.now();
//        int currentHour = currentDateTime.getHour();
        String timeslot = userBooking.getTimeSlot();
        timeslot = timeslot.replaceAll("\\s", "");
        String[] timeslot_array = timeslot.split("-");

        String Start_Time_array = timeslot_array[0];
        String[] Start_Time_hour = Start_Time_array.split(":");
        String Start_hour = Start_Time_hour[0];

//        String End_Time_array = timeslot_array[1];
//        String[] End_Time_hour = End_Time_array.split(":");
//        String End_hour = End_Time_hour[0];

//        LocalTime Start_Time = LocalTime.of(Integer.valueOf(Start_hour),0);
//        LocalTime End_Time = LocalTime.of(Integer.valueOf(End_hour),0);

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int Start_hour_int = Integer.parseInt(Start_hour);

//        if (localTime.isAfter(Start_Time) && localTime.isBefore(End_Time)) {
        if (Start_hour_int <= currentHour) {

            System.out.println("The current time is within the specified range.");
        } else {
            cancel_buttonTextView.setBackgroundTintList(ColorStateList.valueOf
                    (Color.parseColor("#A8D3E9")));
            QR_code_buttonTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
            System.out.println("The current time is outside the specified range.");
        }


//        cancel_buttonTextView.setBackgroundColor(Color.parseColor("#A8D3E9"));

//        tableRow.addView(pay_buttonTextView);
        tableRow.addView(cancel_buttonTextView);

        tableLayout.addView(tableRow);
    }


    private void QR_code_handleButtonClick(String verificationCode) {
        Log.d("QR_code", "Open QR_code called");

        Intent intent = new Intent(ViewBooking.this, QR_Code_Activity.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        intent.putExtra(USER_VERIFICATIONCODE, verificationCode);
        startActivity(intent);
    }

    private void handleButtonClick(long bookingId, long timeId, long codeCheckId, String bookingAmount) {
        Log.d("view book", "bookingAmount:" + bookingAmount);

        UpdateTimeSlot(timeId, bookingAmount);
        RemoveCodeCheck(codeCheckId);
        RemoveBooking(bookingId);
//        RemovePayment(payment.getPaymentId());
//        GetBooking();//*****************************eeeeeeeeeeeeeee
//        Intent intent = new Intent(ViewBooking.this, ViewBooking.class);
//        intent.putExtra(USERID, userID);
//        intent.putExtra(USER_NAME, name);
//        intent.putExtra(USER_EMAIL, email);
//        startActivity(intent);

        Toast toast = Toast.makeText(ViewBooking.this, "Booking have been cancel!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//        startActivityForResult(intent,2);
    }

//    private void RemovePayment(float paymentId) {
//        DatabaseReference paymentFirebase = FirebaseDatabase.getInstance().getReference("Payment");
//        paymentFirebase.orderByChild("paymentId").equalTo(paymentId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    snapshot.getRef().removeValue()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d("Payment", "Data with paymentId " + paymentId + " deleted successfully");
//
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.e("Payment", "Error deleting data with paymentId " + paymentId + ": " + e.getMessage());
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("UserBooking", "Error querying data: " + databaseError.getMessage());
//            }
//        });
//    }

    public void UpdateTimeSlot(long timeId, String peopleAmount) {
        Log.d("currentAttend", "UpdateCurrentAttend func cancel book!");
        DatabaseReference timeSlotDatabase = FirebaseDatabase.getInstance().getReference("TimeSlot");
        timeSlotDatabase.orderByChild("TimeId")
                .equalTo(timeId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("message", "Error getting timeSlot detail!");
                        } else {
                            for (DataSnapshot timeSlotSnapshot : dataSnapshot.getChildren()) {
                                Slot timeslot = timeSlotSnapshot.child("").getValue(Slot.class);
                                Log.d("currentAttend", "UpdateCurrentAttend timeslot!" + timeslot);

                                String currentAttend_str = (String) timeSlotSnapshot.child("Current_Attend").getValue();

                                Log.d("currentAttend", "UpdateCurrentAttend currentAttend_str!" + currentAttend_str);
                                int currentAttend = Integer.valueOf(currentAttend_str);
                                float peopleAmount_long = Float.parseFloat(peopleAmount);
                                currentAttend = currentAttend - (int) peopleAmount_long;//change payment
                                Log.d("currentAttend", "UpdateCurrentAttend currentAttend!" + currentAttend);

                                timeSlotDatabase.child("Time" + timeId).child("Current_Attend").setValue(String.valueOf(currentAttend));
                                Log.d("currentAttend", "current attend update!");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });
    }

    public void RemoveCodeCheck(long codeCheckId) {
        DatabaseReference codeCheckFirebase = FirebaseDatabase.getInstance().getReference("CodeCheck");
        codeCheckFirebase.orderByChild("codeId").equalTo(codeCheckId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("CodeCheck", "Data with codeId " + codeCheckId + " deleted successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("CodeCheck", "Error deleting data with codeId " + codeCheckId + ": " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CodeCheck", "Error querying data: " + databaseError.getMessage());
            }
        });
    }

    public void RemoveBooking(long bookingId) {
        DatabaseReference userBookingFirebase = FirebaseDatabase.getInstance().getReference("UserBooking");
        userBookingFirebase.orderByChild("bookingId").equalTo(bookingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("UserBooking", "Data with bookingId " + bookingId + " deleted successfully");

                                    recreate();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("UserBooking", "Error deleting data with bookingId " + bookingId + ": " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserBooking", "Error querying data: " + databaseError.getMessage());
            }
        });
    }

    private void RemoveNotification(long bookingId, String facilityName, String timeSlot) {

        Intent intent = new Intent(ViewBooking.this, NotificationReceiver.class);

        String bookingId_record_String = String.valueOf(bookingId);
        int bookingId_record_int_request_code = Integer.parseInt(bookingId_record_String);

        intent.putExtra("notification_message", timeSlot);
        intent.putExtra("notification_id", bookingId_record_int_request_code);
        intent.putExtra("notification_facilityName", facilityName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ViewBooking.this, bookingId_record_int_request_code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Log.d("Cancel notification", "at view booking");

            //normal notification for booking
            String channelId = "booking_channel_id";
            String channelName = "Booking Channel";

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Cancel Booking!")
                    .setContentText("You have cancel booking " + facilityName + ".")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You have cancel booking " + facilityName + "."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(100, builder.build());
            Log.d("Cancel notification", "display,at view booking page");

        } else {
            Log.d("Cancel notification", "not ok at view booking");

        }

    }

}