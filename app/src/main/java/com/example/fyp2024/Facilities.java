package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fyp2024.DB.Facility;
//import com.example.fyp2024.DB.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Facilities extends AppCompatActivity {

    DatabaseReference mDatabase;
    DatabaseReference facilitiesDatabase;
    String name;
    String email;
    String userID;
    int current_Attend_total;

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String FACILITY_ID = "com.example.fyp2024.FACILITY_ID";
    public static final String FACILITY_NAME = "com.example.fyp2024.FACILITIES_NAME";
    public static final String TICKET_LIMIT = "com.example.fyp2024.TICKET_LIMIT";
    String ticket_limit;

    ArrayList<Facility> facilities = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);


        Log.d("Facilities Page", "working");

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(Home.USERID);
            name = intent.getStringExtra(Home.USER_NAME);
            email = intent.getStringExtra(Home.USER_EMAIL);
            ticket_limit = intent.getStringExtra(Home.TICKET_LIMIT);
            Log.d("ticket_limit","ticket_limit:" + ticket_limit);

//            Toast.makeText(Facilities.this, "Id!"+userID, Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        facilitiesDatabase = FirebaseDatabase.getInstance().getReference("Facilities");
        facilitiesDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(Facilities.this, " getting Facilities data!", Toast.LENGTH_SHORT).show();

                    Log.d("Facilities Page", "getting Facilities data!");
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Object FacilityIdObject = childSnapshot.child("FacilitiesId").getValue();
                        String FacilityId_rec = String.valueOf(FacilityIdObject);
                        String Facilityname = childSnapshot.child("name").getValue(String.class);
                        long FacilityId_long = Long.parseLong(FacilityId_rec);
                        Facility facility = new Facility(FacilityId_long, Facilityname);
                        facilities.add(facility);
                    }
                    SetFacilities();
                } else {
                    Toast.makeText(Facilities.this, "error getting Facilities data!", Toast.LENGTH_SHORT).show();

                    Log.d("Facilities Page", "error getting Facilities data!");
                }
            }
        });
    }

    private void handleButtonClick(long FacilityId, String FacilityName) {
        Intent intent = new Intent(Facilities.this, TimeSlot.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        intent.putExtra(FACILITY_ID, String.valueOf(FacilityId));
        intent.putExtra(FACILITY_NAME, FacilityName);
        intent.putExtra(TICKET_LIMIT, ticket_limit);
        startActivity(intent);
//        Log.d("message", "id = " + userId + ", name = " + userName);

    }

    private void SetFacilities() {
//        System.out.println("working");

        Log.d("Facilities Page", "SetFacilities");
        ListView FacilitiesListView = findViewById(R.id.Facilities_List);
        ArrayAdapter<Facility> adapter = new ArrayAdapter<Facility>(this, R.layout.facility_item, R.id.facility_button, facilities) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                System.out.println(facilities);
                Button facility_button = view.findViewById(R.id.facility_button);


                Facility Facility = getItem(position);

                System.out.println(Facility);

//                GetCurrentAttend(Facility.getFacilityId());
//
//                System.out.println("current_Attend_total-button");
//                System.out.println(current_Attend_total);
//
//                facility_button.setText(String.valueOf(Facility.getFacilityName())+" ("+current_Attend_total+")");

                facility_button.setText(String.valueOf(Facility.getFacilityName()));
                facility_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(Facility.getFacilityId(), Facility.getFacilityName());
                    }
                });
                return view;
            }

            private void GetCurrentAttend(long facilityId) {///may use

                Log.d("Facilities Page", "GetCurrentAttend");
                DatabaseReference timeSlotFirebase = FirebaseDatabase.getInstance().getReference("TimeSlot");
                timeSlotFirebase.orderByChild("FacilitiesId").equalTo(facilityId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String current_Attend = (String)snapshot.child("Current_Attend").getValue();
                            System.out.println("current_Attend");
                            System.out.println(current_Attend);
                            current_Attend_total+=Integer.valueOf(current_Attend);

                            System.out.println(current_Attend_total);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("GetCurrentAttend", "Error querying data: " + databaseError.getMessage());
                    }
                });
            }
        };
        FacilitiesListView.setAdapter(adapter);
    }


    public void Back(View view) {
        Intent intent = new Intent(Facilities.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }
}