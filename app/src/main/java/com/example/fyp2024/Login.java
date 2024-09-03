package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2024.DB.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    EditText Email;
    TextView Password;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view){
        database = FirebaseDatabase.getInstance().getReference("User");

        EditText Email = (EditText) findViewById(R.id.Login_email_Input);
        TextView Password = (EditText) findViewById(R.id.Login_password_Input);

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    System.out.println("User not found");
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

//                        Log.d("FirebaseData", "ChildSnapshot: " + childSnapshot.getValue());
//                        User user = new User();
//                        user = childSnapshot.getValue(User.class);
//                        if (user != null) {
//                            Log.d("FirebaseData", "User: " + user.toString());
//                        } else {
//                            Log.d("FirebaseData", "User is null");
//                        }

//                        User user = childSnapshot.getValue(User.class);
                        String password_rec = (String) childSnapshot.child("password").getValue();
                        Object userIdObject = childSnapshot.child("userId").getValue();
                        String userId_rec = String.valueOf(userIdObject);

                        String email_rec = (String) childSnapshot.child("email").getValue();
                        String name_rec = (String) childSnapshot.child("name").getValue();

                        if(password.equals(password_rec)){
                            //login success
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra(USERID, userId_rec);
                            intent.putExtra(USER_NAME, name_rec);
                            intent.putExtra(USER_EMAIL, email_rec);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Login.this, "Invalid Email or Password2!", Toast.LENGTH_SHORT).show();
                        }
//                        System.out.println(user);
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid Email or Password1!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error getting data: " + databaseError.getMessage());
            }
        });

    }

    public void Go_to_Register(View view){//go to register page
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);

    }

    public void StaffClient(View view){
        Intent intent = new Intent(Login.this, StaffLogin.class);
        startActivity(intent);
    }


}