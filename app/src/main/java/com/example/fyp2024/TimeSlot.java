package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.Slot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class TimeSlot extends AppCompatActivity {


    String name;
    String email;
    String userID;
    String facilityID;
    String facilityName;
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String FACILITY_ID = "com.example.fyp2024.FACILITY_ID";
    public static final String FACILITY_NAME = "com.example.fyp2024.FACILITY_NAME";
    public static final String TIME_ID = "com.example.fyp2024.TIME_ID";
    public static final String TIMESLOT = "com.example.fyp2024.TIMESLOT";
    public static final String MAX_ATTEND = "com.example.fyp2024.MAX_ATTEND";
    public static final String CURRENT_ATTEND = "com.example.fyp2024.CURRENT_ATTEND";
    public static final String TICKET_LIMIT = "com.example.fyp2024.TICKET_LIMIT";
    String ticket_limit;


    ArrayList<Slot> timeslots = new ArrayList<>();

    DatabaseReference mDatabase;
    DatabaseReference timeslotDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);


        Log.d("TimeSlot Page", "TimeSlot Page");

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(Facilities.USERID);
            name = intent.getStringExtra(Facilities.USER_NAME);
            email = intent.getStringExtra(Facilities.USER_EMAIL);
            facilityID = intent.getStringExtra(Facilities.FACILITY_ID);
            facilityName = intent.getStringExtra(Facilities.FACILITY_NAME);
            ticket_limit = intent.getStringExtra(Facilities.TICKET_LIMIT);

//            Toast.makeText(TimeSlot.this, "Id!" + userID, Toast.LENGTH_SHORT).show();

            TextView FacilityNameSelected = findViewById(R.id.FacilityNameSelected);
            FacilityNameSelected.setText("You are currently booking: " + facilityName);
            GetTimeSlots();
        } else {
            Toast.makeText(TimeSlot.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();

            Log.d("TimeSlot Page", "Data missed, such as Id!");
        }


    }

    public void GetTimeSlots() {

        Log.d("TimeSlot Page", "GetTimeSlots");
        timeslotDatabase = FirebaseDatabase.getInstance().getReference("TimeSlot");
        timeslotDatabase.orderByChild("FacilitiesId").equalTo(Long.valueOf(facilityID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Object TimeId_object = childSnapshot.child("TimeId").getValue();
                        String TimeId_rec = String.valueOf(TimeId_object);
                        System.out.println("TimeId_rec " + TimeId_rec);
                        long TimeId_long = Long.parseLong(TimeId_rec);

                        Object FacilitiesId_object = childSnapshot.child("FacilitiesId").getValue();
                        String FacilitiesId_rec = String.valueOf(FacilitiesId_object);
                        System.out.println("FacilitiesId_rec " + FacilitiesId_rec);
                        long FacilitiesId_long = Long.parseLong(FacilitiesId_rec);

                        String FacilitiesName = (String) childSnapshot.child("FacilitiesName").getValue();
                        System.out.println("FacilitiesName " + FacilitiesName);
                        String Time_Start = (String) childSnapshot.child("Time_Start").getValue();
                        System.out.println("Time_Start " + Time_Start);
                        String Time_End = (String) childSnapshot.child("Time_End").getValue();
                        System.out.println("Time_End " + Time_End);
                        String Max_Attend = (String) childSnapshot.child("Max_Attend").getValue();
                        System.out.println("Max_Attend " + Max_Attend);
                        String Current_Attend = (String) childSnapshot.child("Current_Attend").getValue();
                        System.out.println("Current_Attend " + Current_Attend);

//                        String password_rec = (String) childSnapshot.child("password").getValue();
//                        Object userIdObject = childSnapshot.child("userId").getValue();
//                        String userId_rec = String.valueOf(userIdObject);

//                        Toast.makeText(TimeSlot.this, "Id!"+userId_rec, Toast.LENGTH_SHORT).show();
                        Slot timeslot = new Slot(TimeId_long, FacilitiesId_long, FacilitiesName, Time_Start, Time_End, Max_Attend, Current_Attend);
                        timeslots.add(timeslot);
                        System.out.println(timeslot);
                    }

                    Collections.sort(timeslots, new Comparator<Slot>() {//sort time slots
                        @Override
                        public int compare(Slot s1, Slot s2) {
                            return Long.compare(s1.getTimeId(),s2.getTimeId());
                        }
                    });

                    System.out.println(timeslots);
                    SetTimeSlotTable();
                } else {
                    Toast.makeText(TimeSlot.this, "This data FacilitiesId " + "2 " + "not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error getting data: " + databaseError.getMessage());
            }
        });
    }

//    public void SetTimeSoltTable(){
//        TableLayout tableLayout = findViewById(R.id.tableLayout);
//
//        for (Slot solt : timesolts) {
//            TableRow tableRow = new TableRow(this);
//
//            Field[] fields = Slot.class.getDeclaredFields();
//
//            Log.d("message","field: "+fields);
//            for (Field field : fields) {
//                try {
//                    Log.d("message","field: "+field);
//                    // 设置字段可访问
//                    field.setAccessible(true);
//
//                    // 创建 TextView 并设置字段值
//                    TextView textView = new TextView(this);
//                    textView.setText(String.valueOf(field.get(solt)));
//                    textView.setPadding(8, 8, 8, 8);
//                    tableRow.addView(textView);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                    Log.d("message","erroe");
//                }
//            }
//
////            for (int i = 0; i < 6;i++) {
////                TextView textView = new TextView(this);
////                textView.setText(cellData);
////                textView.setPadding(8, 8, 8, 8);
////                tableRow.addView(textView);
////            }
//            TextView textView = new TextView(this);
//            textView.setText("BUTTON");
//            textView.setPadding(8, 8, 8, 8);
//            tableRow.addView(textView);
//
//            tableLayout.addView(tableRow);
//        }
//    }

    public void SetTimeSlotTable() {

        Log.d("TimeSlot Page", "SetTimeSlotTable");
        ListView TimeSoltListView = findViewById(R.id.TimeSlot_List);
        ArrayAdapter<Slot> adapter = new ArrayAdapter<Slot>(this, R.layout.timesolt_item, R.id.timesolt_button, timeslots) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                System.out.println(timeslots);
                Button timesolt_button = view.findViewById(R.id.timesolt_button);

//                Log.d("message", "working");

                Slot slot = getItem(position);

                System.out.println(slot);

                String timeStart = slot.getTime_Start();
                String timeEnd = slot.getTime_End();
                String currentAttend = slot.getCurrent_Attend();
                String maxAttend = slot.getMax_Attend();
                long timeId = slot.getTimeId();

                Calendar now = Calendar.getInstance();
                int currentHour = now.get(Calendar.HOUR_OF_DAY);

//                LocalTime localTime = LocalTime.now();

//                LocalTime Start_Time = LocalTime.of(Integer.valueOf(timeStart),0);
////                LocalTime Start_Time = LocalTime.of(0,0);
//
//                LocalTime End_Time = LocalTime.of(Integer.valueOf(timeEnd),0);

//                for(int i  = 0; i< timeslots.size();i++){

                    int start_hour = Integer.valueOf(timeStart);

//                    LocalTime Start_Time = LocalTime.of(Integer.valueOf(timeslots.get(i).getTime_Start()),0);
//                    LocalTime End_Time = LocalTime.of(Integer.valueOf(timeslots.get(i).getTime_End()),0);

                    if (start_hour <= currentHour) {
                        timesolt_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
                        timesolt_button.setFocusable(false);
                        timesolt_button.setClickable(false);
                        timesolt_button.setEnabled(false);
                        System.out.println("The current time is within the specified range.");//not allow booking past event
                    }else{
                        timesolt_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F42C3")));
                        timesolt_button.setFocusable(true);
                        timesolt_button.setClickable(true);
                        timesolt_button.setEnabled(true);
                        System.out.println("The current time is outside the specified range.");//allow booking not start event
                    }
//                    if (Start_Time.isBefore(localTime) && (localTime.isAfter(Start_Time) && localTime.isBefore(End_Time))) {//0:00 - current event end time
//
//                        timesolt_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A8D3E9")));
//
//                        timesolt_button.setFocusable(false);
//                        timesolt_button.setClickable(false);
//                        System.out.println("The current time is within the specified range.");//not allow booking past event
//                    } else {
//                        timesolt_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F42C3")));
//
//                        timesolt_button.setFocusable(true);
//                        timesolt_button.setClickable(true);
//                        System.out.println("The current time is outside the specified range.");//allow booking not start event
//                    }
//                }



//try set button disable by time pass
                timesolt_button.setText(timeStart + ":00 - " + timeEnd + ":00 (" + currentAttend + "/" + maxAttend + ") booked");
                timesolt_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(timeStart, timeEnd, currentAttend, maxAttend, timeId);
                    }
                });
                return view;
            }
        };
        TimeSoltListView.setAdapter(adapter);
    }

    private void handleButtonClick(String timeStart, String timeEnd, String currentAttend, String maxAttend, long timeId) {
        if (currentAttend.equals(maxAttend)) {
            Log.d("message", "This time slot is full of Booking, please select another booking!");
        } else {
            Intent intent = new Intent(TimeSlot.this, BookingAmount.class);//new booking page
            intent.putExtra(USERID, userID);
            intent.putExtra(USER_NAME, name);
            intent.putExtra(USER_EMAIL, email);
            intent.putExtra(FACILITY_ID, facilityID);
            intent.putExtra(FACILITY_NAME, facilityName);
            intent.putExtra(TIME_ID, String.valueOf(timeId));
            intent.putExtra(TIMESLOT, (timeStart+":00 - "+timeEnd+":00"));
            intent.putExtra(CURRENT_ATTEND, currentAttend);
            intent.putExtra(MAX_ATTEND, maxAttend);
            intent.putExtra(TICKET_LIMIT, ticket_limit);
            startActivity(intent);
            Log.d("message", "no ");
        }
    }

    public void BackBtn(View view){
        Intent intent = new Intent(TimeSlot.this, Facilities.class);//new booking page
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }


}