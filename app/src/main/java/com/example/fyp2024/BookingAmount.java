package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class BookingAmount extends AppCompatActivity {

    double adult_price = 5.5;
    double children_price = 5.5;
    double total_price = 0;
    float total_amount_of_people = 0;

    TextView AdultPriceText;
    EditText AdultAmount;
    TextView ChildrenPriceText;
    EditText ChildrenAmount;
    TextView TotalPrice;

    String name;
    String email;
    String userID;
    String facilityID;
    String facilityName;
    String timeId;
    String timeSlot;
    String current_attend;
    String max_attend;
    String verification_code;
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String FACILITY_ID = "com.example.fyp2024.FACILITY_ID";
    public static final String FACILITY_NAME = "com.example.fyp2024.FACILITY_NAME";
    public static final String TIME_ID = "com.example.fyp2024.TIME_ID";
    public static final String TIMESLOT = "com.example.fyp2024.TIMESLOT";
    public static final String TOTAL_PRICE = "com.example.fyp2024.TOTAL_PRICE";
    public static final String TOTAL_PEOPLE = "com.example.fyp2024.TOTAL_PEOPLE";
    public static final String TICKET_LIMIT = "com.example.fyp2024.TICKET_LIMIT";
    String ticket_limit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_amount);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(TimeSlot.USERID);
            name = intent.getStringExtra(TimeSlot.USER_NAME);
            email = intent.getStringExtra(TimeSlot.USER_EMAIL);
            facilityID = intent.getStringExtra(TimeSlot.FACILITY_ID);
            facilityName = intent.getStringExtra(TimeSlot.FACILITY_NAME);
            timeId = intent.getStringExtra(TimeSlot.TIME_ID);
            timeSlot = intent.getStringExtra(TimeSlot.TIMESLOT);
            current_attend = intent.getStringExtra(TimeSlot.CURRENT_ATTEND);
            max_attend = intent.getStringExtra(TimeSlot.MAX_ATTEND);
            ticket_limit = intent.getStringExtra(TimeSlot.TICKET_LIMIT);

            Log.d("ticket_limit","ticket_limit:" + ticket_limit);

            Log.d("check attend","current_attend" + current_attend+", max_attend"+max_attend);

            TextView EventSelected = findViewById(R.id.EventSelected);
            EventSelected.setText("You are currently booking: " + facilityName + " (" + timeSlot + ")" + "\n" +
                    "Available: ("+ current_attend + "/" + max_attend + ")");
        } else {
            Toast.makeText(BookingAmount.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

        AdultPriceText = (TextView) findViewById(R.id.AdultPriceText);
        AdultAmount = (EditText) findViewById(R.id.AdultAmount);
        ChildrenPriceText = (TextView) findViewById(R.id.ChildrenPriceText);
        ChildrenAmount = (EditText) findViewById(R.id.ChildrenAmount);
        TotalPrice = (TextView) findViewById(R.id.TotalPrice);

        AdultPriceText.setText("Adult Amount");
        ChildrenPriceText.setText("Children Amount");

        calculateAndDisplay();

        AdultAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                calculateAndDisplay();
                return false;
            }
        });
        ChildrenAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                calculateAndDisplay();
                return false;
            }
        });
    }

    public void calculateAndDisplay() {
        if(AdultAmount.getText().toString()!= null && !AdultAmount.getText().toString().isEmpty() &&
                ChildrenAmount.getText().toString()!= null && !ChildrenAmount.getText().toString().isEmpty()) {

            float adult_amount = Float.parseFloat(AdultAmount.getText().toString());
            float children_amount = Float.parseFloat(ChildrenAmount.getText().toString());
            total_amount_of_people = adult_amount+children_amount;

            double children_total = children_amount * children_price;
            double adult_total = adult_amount * adult_price;

            total_price = children_total + adult_total;
            TotalPrice.setText("Total amount of people: " + total_amount_of_people);
        }else{
            Toast.makeText(BookingAmount.this, "People amount can't be blank!", Toast.LENGTH_SHORT).show();
//            Toast toast = Toast.makeText(this, "People amount can't be blank!", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }

    }

    public void GotoBooking(View view){
        if(AdultAmount.getText().toString()!= null && !AdultAmount.getText().toString().isEmpty() &&
                ChildrenAmount.getText().toString()!= null && !ChildrenAmount.getText().toString().isEmpty()) {
            if( ((int)total_amount_of_people + Integer.valueOf(current_attend)) > Integer.valueOf(max_attend)) {
                Toast.makeText(BookingAmount.this, "Too much people!", Toast.LENGTH_SHORT).show();
                Log.d("check amount people","max_attend:"+max_attend+",current_attend"+current_attend);

            }else {
                double ticket_limit_Double = Double.parseDouble(ticket_limit);

                if((int)total_amount_of_people > (int)ticket_limit_Double){
                    Toast.makeText(BookingAmount.this, "People amount over than ticket limit!", Toast.LENGTH_SHORT).show();
                    Log.d("check amount people","total_amount_of_people:"+total_amount_of_people+",ticket_limit"+ticket_limit);
                }else{

                    Intent intent = new Intent(BookingAmount.this, BookEvent.class);
                    intent.putExtra(USERID, userID);
                    intent.putExtra(USER_NAME, name);
                    intent.putExtra(USER_EMAIL, email);
                    intent.putExtra(FACILITY_ID, facilityID);
                    intent.putExtra(FACILITY_NAME, facilityName);
                    intent.putExtra(TIMESLOT, timeSlot);
                    intent.putExtra(TIME_ID, timeId);
//                intent.putExtra(TOTAL_PRICE, String.valueOf(total_price));
                    intent.putExtra(TOTAL_PEOPLE, String.valueOf(total_amount_of_people));
                    startActivity(intent);
                }

            }
        }else{
//            Toast.makeText(BookingAmount.this, "People amount can't be blank!", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(this, "People amount can't be blank!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void BackBtn(View view){
            Intent intent = new Intent(BookingAmount.this, Home.class);
            intent.putExtra(USERID, userID);
            intent.putExtra(USER_NAME, name);
            intent.putExtra(USER_EMAIL, email);
            startActivity(intent);
            //may edit to timeslot page

    }
//do a check for booking limit, may be use extra way to pass data(max attend)
//use timeId check userbooking, take sum of booking amount, than use this sum + enter value...

}