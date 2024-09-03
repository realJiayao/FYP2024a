package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.fyp2024.DB.Payment;
import com.example.fyp2024.DB.UserBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

public class ViewBooking_Staff extends AppCompatActivity {
    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";
    String name;
    String email;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_staff);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(StaffHome.STAFF_USERID);
            name = intent.getStringExtra(StaffHome.STAFF_USER_NAME);
            email = intent.getStringExtra(StaffHome.STAFF_USER_EMAIL);

        } else {
            Toast.makeText(ViewBooking_Staff.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

        GetBooking();


    }

    public void ViewBooking_BackBtn(View view) {
        Intent intent = new Intent(ViewBooking_Staff.this, StaffHome.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);
    }

    public void GetBooking() {
        DatabaseReference userBookingDatabase = FirebaseDatabase.getInstance().getReference("UserBooking");
        userBookingDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                        UserBooking userBooking = bookingSnapshot.child("").getValue(UserBooking.class);

                        System.out.println("bookingSnapshot");
                        System.out.println(userBooking);
//                        userBookings.add(userBooking);
                        GetPayment(userBooking);
                    }
                } else {
                    Log.e("FirebaseData", "Error getting data", task.getException());
                }
            }
        });

    }

    public void GetPayment(UserBooking userBooking){
        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
//no booking id in payment column
        paymentDatabase.orderByChild("userId")
                .equalTo(Long.valueOf(userBooking.getUserId())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("payment_database", "Error getting payment detail!");
                        } else {
                            for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                                Payment payment = paymentSnapshot.child("").getValue(Payment.class);
                                System.out.println("paymentSnapshot");
                                System.out.println(payment);

                                SetBookingDetail(userBooking,payment);
                                //get payment
//                                SetBookingDetail(userBooking);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });


    }

    public void SetBookingDetail(UserBooking userBooking, Payment payment) {

        System.out.println("setting detail");
        TableLayout tableLayout = findViewById(R.id.tableLayout_viewBooking_staff);
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

        //payment
        Log.d("ViewBooking Pay","payid:"+payment.getPaymentId());

        float paymentID = payment.getPaymentId();
        Log.d("paymentID","paymentID:"+paymentID);

        int paymentID_int = (int)paymentID;
        Log.d("paymentID_int","paymentID_int:"+paymentID_int);


        TextView textView3 = new TextView(this);
        textView3.setText(payment.getPayment());
        textView3.setPadding(8, 8, 8, 8);
        textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView3);
        System.out.println(payment.getPayment());
//
        TextView textView4 = new TextView(this);
        String people_amount = payment.getPeople_amount();
        float people_amount_float = Float.parseFloat(people_amount);
        Log.d("people_amount_float","people_amount_float:"+people_amount_float);

        int people_amount_int = (int)people_amount_float;
        Log.d("people_amount_int","people_amount_int:"+people_amount_int);

        textView4.setText(""+people_amount_int);
        textView4.setPadding(8, 8, 8, 8);
        textView4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView4);
        System.out.println(payment.getPeople_amount());

        TextView textView5 = new TextView(this);
        textView5.setText(payment.getPrice());
        textView5.setPadding(8, 8, 8, 8);
        textView5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tableRow.addView(textView5);
        System.out.println(payment.getPrice());

//        Button pay_buttonTextView = new Button(this);//pay button
//        pay_buttonTextView.setText("Pay");
//        pay_buttonTextView.setPadding(8, 8, 8, 8);
//        pay_buttonTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                handleButtonClick(userBooking.getBookingId(), userBooking.getTimeId(), userBooking.getCodeCheckId());
//                Intent intent = new Intent(ViewBooking_Staff.this, Pay.class);
//                intent.putExtra(USERID, userID);
//                intent.putExtra(USER_NAME, name);
//                intent.putExtra(USER_EMAIL, email);
//                intent.putExtra(BOOKING_ID, String.valueOf(userBooking.getBookingId()));
//                intent.putExtra(PAYMENT_ID, String.valueOf(paymentID_int));
//
//                startActivity(intent);
//            }
//        });

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
                handleButtonClick(userBooking.getBookingId(), userBooking.getTimeId(), userBooking.getCodeCheckId(), payment);
            }
        });

//        LocalTime localTime = LocalTime.now();
////        int currentHour = currentDateTime.getHour();
//        String timeslot = userBooking.getTimeSlot();
//        timeslot = timeslot.replaceAll("\\s", "");
//        String[] timeslot_array = timeslot.split("-");
//
//        String Start_Time_array = timeslot_array[0];
//        String[] Start_Time_hour = Start_Time_array.split(":");
//        String Start_hour = Start_Time_hour[0];
//
//
//        String End_Time_array = timeslot_array[1];
//        String[] End_Time_hour = End_Time_array.split(":");
//        String End_hour = End_Time_hour[0];
//
//
//
//        LocalTime Start_Time = LocalTime.of(Integer.valueOf(Start_hour),0);
//        LocalTime End_Time = LocalTime.of(Integer.valueOf(End_hour),0);
//
//
//        if (localTime.isAfter(Start_Time) && localTime.isBefore(End_Time)) {
//            cancel_buttonTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//            System.out.println("The current time is within the specified range.");
//        } else {
//            System.out.println("The current time is outside the specified range.");
//        }


//        cancel_buttonTextView.setBackgroundColor(Color.parseColor("#A8D3E9"));

//        tableRow.addView(pay_buttonTextView);
        tableRow.addView(cancel_buttonTextView);

        tableLayout.addView(tableRow);
    }

    private void handleButtonClick(long bookingId, long timeId, long codeCheckId, Payment payment) {

        UpdateTimeSlot(timeId, payment.getPeople_amount());
        RemoveCodeCheck(codeCheckId);
        RemoveBooking(bookingId);
//        RemovePayment(payment.getPaymentId());//************************92
//        GetBooking();
        Intent intent = new Intent(ViewBooking_Staff.this, ViewBooking_Staff.class);
        intent.putExtra(STAFF_USERID, userID);
        intent.putExtra(STAFF_USER_NAME, name);
        intent.putExtra(STAFF_USER_EMAIL, email);
        startActivity(intent);

        Toast toast = Toast.makeText(ViewBooking_Staff.this, "Booking have been cancel!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//        startActivityForResult(intent,2);
    }

    private void RemovePayment(float paymentId) {
        DatabaseReference paymentFirebase = FirebaseDatabase.getInstance().getReference("Payment");
        paymentFirebase.orderByChild("paymentId").equalTo(paymentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Payment", "Data with paymentId " + paymentId + " deleted successfully");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Payment", "Error deleting data with paymentId " + paymentId + ": " + e.getMessage());
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

    public void UpdateTimeSlot(long timeId, String peopleAmount) {
        Log.d("message", "UpdateCurrentAttend func cancel book!");
        DatabaseReference timeSlotDatabase = FirebaseDatabase.getInstance().getReference("TimeSlot");
        timeSlotDatabase.orderByChild("TimeId")
                .equalTo(timeId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("message", "Error getting timeSlot detail!");
                        } else {
                            for (DataSnapshot timeSlotSnapshot : dataSnapshot.getChildren()) {
                                String currentAttend_str = (String) timeSlotSnapshot.child("Current_Attend").getValue();
                                int currentAttend = Integer.valueOf(currentAttend_str);
                                float peopleAmount_long = Float.parseFloat(peopleAmount);
                                currentAttend = currentAttend-(int)peopleAmount_long;//change payment
                                timeSlotDatabase.child("Time" + timeId).child("Current_Attend").setValue(String.valueOf(currentAttend));
                                Log.d("message", "current attend update!");
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



}