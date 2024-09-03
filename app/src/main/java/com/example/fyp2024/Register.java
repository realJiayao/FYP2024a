package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";

    EditText Email;
    EditText Password;
    EditText Phone;
    EditText Name;
    DatabaseReference mDatabase;
    DatabaseReference userDatabase;

    String email;
    String password;
    String phone;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void Register(View view) {


        //----------------------------------------------
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Email = (EditText) findViewById(R.id.Register_email_Input);
        Password = (EditText) findViewById(R.id.Register_password_Input);
        Phone = (EditText) findViewById(R.id.Register_phone_Input);
        Name = (EditText) findViewById(R.id.Register_name_Input);

        email = Email.getText().toString();
        password = Password.getText().toString();
        phone = Phone.getText().toString();
        name = Name.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !name.isEmpty()){
            userDatabase = FirebaseDatabase.getInstance().getReference("User");
            userDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(Register.this, "This Email is exist, please use another email!", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatabase.child("User").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, " getting data!", Toast.LENGTH_SHORT).show();

                                    DataSnapshot dataSnapshot = task.getResult();
                                    long Count_record = dataSnapshot.getChildrenCount();
                                    Toast.makeText(Register.this, " getting data! have " + Count_record, Toast.LENGTH_SHORT).show();

                                    long USER_ID = Count_record;
                                    long new_Count_record = Count_record + 1;

                                    Toast.makeText(Register.this, " now!" + new_Count_record, Toast.LENGTH_SHORT).show();

//                    Map<String, User> users = new HashMap<>();
//                    users.put(""+USER_ID, new User("name","password","phone","email"));

//                    mDatabase.setValue(users);
                                    User user = new User(USER_ID, name, password, phone, email);
//                    mDatabase.child("User").child(""+USER_ID).setValue(USER_ID);
//                    mDatabase.child("User").child("User "+USER_ID).child("").setValue(user);
                                    mDatabase.child("User").child("User" + USER_ID).setValue(user);

                                    Intent intent = new Intent(Register.this, Home.class);
                                    String USER_ID_string = String.valueOf(USER_ID);
                                    intent.putExtra(USERID, USER_ID_string);
                                    intent.putExtra(USER_NAME, name);
                                    intent.putExtra(USER_EMAIL, email);
                                    startActivity(intent);


                                    //to Home page!!!!!!!!

//                    final AtomicInteger count = new AtomicInteger();
//                    int newCount = count.incrementAndGet();
//                    System.out.println("Added " + dataSnapshot.getKey() + ", count is " + newCount);

//                    if (dataSnapshot.exists()) {
//                        String loss = dataSnapshot.child("loss").getValue(String.class);
//                        String name = dataSnapshot.child("name").getValue(String.class);
//                        String ties = dataSnapshot.child("ties").getValue(String.class);
//                        String win = dataSnapshot.child("win").getValue(String.class);
//
//                        Log.d("FirebaseData", "Loss: " + loss);
//                        Log.d("FirebaseData", "Name: " + name);
//                        Log.d("FirebaseData", "Ties: " + ties);
//                        Log.d("FirebaseData", "Win: " + win);
//                    }
                                } else {
                                    Log.e("FirebaseData", "Error getting data", task.getException());
                                    Toast.makeText(Register.this, "Error getting data!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Error getting data: " + databaseError.getMessage());
                }
            });
        }else{
            Toast.makeText(Register.this, "Field can't not be blank!", Toast.LENGTH_SHORT).show();
        }
    }


    public void Login(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");

        EditText Email = (EditText) findViewById(R.id.Login_email_Input);
        TextView Password = (EditText) findViewById(R.id.Login_password_Input);

        String email = Email.getText().toString();
        String password = Password.getText().toString();


    }


    public void Go_to_Login(View view) {

        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);

    }

}