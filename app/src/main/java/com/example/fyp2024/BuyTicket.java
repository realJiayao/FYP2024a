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

import com.example.fyp2024.DB.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyTicket extends AppCompatActivity {
    int max_ticket = 1000;
    int current_ticket;
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
    String ticket_detail;
    String price_detail;
    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    public static final String TOTAL_PRICE = "com.example.fyp2024.TOTAL_PRICE";
    public static final String TOTAL_PEOPLE = "com.example.fyp2024.TOTAL_PEOPLE";
    public static final String TICKET_DETAIL = "com.example.fyp2024.TICKET_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(Home.USERID);
            name = intent.getStringExtra(Home.USER_NAME);
            email = intent.getStringExtra(Home.USER_EMAIL);

            TextView EventSelected = findViewById(R.id.EventSelected);

            ticket_detail = "Available: ("+ current_ticket + "/" + max_ticket + ")";
            EventSelected.setText("You are currently buying ticket"+ "\n" + ticket_detail);

        } else {
            Toast.makeText(BuyTicket.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }

        AdultPriceText = (TextView) findViewById(R.id.AdultPriceText);
        AdultAmount = (EditText) findViewById(R.id.AdultAmount);
        ChildrenPriceText = (TextView) findViewById(R.id.ChildrenPriceText);
        ChildrenAmount = (EditText) findViewById(R.id.ChildrenAmount);
        TotalPrice = (TextView) findViewById(R.id.TotalPrice);

        AdultPriceText.setText("Adult Price("+adult_price+"/person)");
        ChildrenPriceText.setText("Children Price("+children_price+"/person)");

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
            price_detail = "Children Total Price: " + children_total + "\n" +
                    "Adult Total Price: " + adult_total + "\n" +
                    "Total amount of people: " + total_amount_of_people + "\n" +
                    "Total Price: " + total_price;
            TotalPrice.setText(price_detail);
        }else{
            Toast.makeText(BuyTicket.this, "People amount can't be blank!", Toast.LENGTH_SHORT).show();
//            Toast toast = Toast.makeText(this, "People amount can't be blank!", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }

    }

    public void ButTicket(View view){
        if(AdultAmount.getText().toString()!= null && !AdultAmount.getText().toString().isEmpty() &&
                ChildrenAmount.getText().toString()!= null && !ChildrenAmount.getText().toString().isEmpty()) {
            if( (int)total_amount_of_people  > max_ticket) {
                Toast.makeText(BuyTicket.this, "Too much people!", Toast.LENGTH_SHORT).show();
                Log.d("check amount people","max_ticket:"+max_ticket+",current_attend"+current_ticket);

            }else {
                ticket_detail+="\n" +price_detail;
                    Intent intent = new Intent(BuyTicket.this, BuyTicketConfirm.class);
                intent.putExtra(USERID, userID);
                intent.putExtra(USER_NAME, name);
                intent.putExtra(USER_EMAIL, email);
                intent.putExtra(TOTAL_PRICE, String.valueOf(total_price));
                intent.putExtra(TOTAL_PEOPLE, String.valueOf(total_amount_of_people));;
                intent.putExtra(TICKET_DETAIL, ticket_detail);
                startActivity(intent);
            }
        }else{
//            Toast.makeText(BookingAmount.this, "People amount can't be blank!", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(this, "People amount can't be blank!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void BackBtn(View view){
        Intent intent = new Intent(BuyTicket.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
        //may edit to timeslot page

    }

    public void CurrentTicket(){
        Log.d("CurrentTicket","CurrentTicket");

        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");

        paymentDatabase.orderByChild("people_amount")
                .equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("payment_database", "Error getting payment detail!");
                        } else {
                            for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                                Payment payment = paymentSnapshot.child("").getValue(Payment.class);
                                System.out.println("paymentSnapshot");
                                System.out.println(payment);
                                int people = Integer.valueOf(payment.getPeople_amount());
//                                current_ticket+=(int)payment.getPeople_amount();
                                current_ticket+=people;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Error getting data: " + databaseError.getMessage());
                    }
                });

    }


}