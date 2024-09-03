package com.example.fyp2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp2024.DB.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateUser extends AppCompatActivity {

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";
    DatabaseReference mDatabase;
    EditText Email;
    EditText Password ;
    EditText Phone;
    EditText Name;

    String name;
    String email;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Email = (EditText) findViewById(R.id.Update_email_Input);
        Password = (EditText) findViewById(R.id.Update_password_Input);
        Phone = (EditText) findViewById(R.id.Update_phone_Input);
        Name = (EditText) findViewById(R.id.Update_name_Input);

        if (getIntent() != null) {
            Intent intent = getIntent();
            UserID = intent.getStringExtra(Login.USERID);
            name = intent.getStringExtra(Home.USER_NAME);
            email = intent.getStringExtra(Home.USER_EMAIL);
        }


        mDatabase.child("User").child("User"+UserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateUser.this, " getting data!", Toast.LENGTH_SHORT).show();

                    DataSnapshot dataSnapshot = task.getResult();
//                    long count = dataSnapshot.getChildrenCount();
//                    Toast.makeText(UpdateUser.this, " getting data! have " + count, Toast.LENGTH_SHORT).show();
//
//                    long USER_ID = count+1;
//
//                    Toast.makeText(UpdateUser.this, " now!"+USER_ID, Toast.LENGTH_SHORT).show();

                    String email = dataSnapshot.child("email").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    Email.setText(email);
                    Password.setText(password);
                    Phone.setText(phone);
                    Name.setText(name);

                    Email.setFocusable(false);
                    Email.setClickable(false);

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
                    Toast.makeText(UpdateUser.this, "Error getting data!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void Update(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("User").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateUser.this, " getting data!", Toast.LENGTH_SHORT).show();

                    DataSnapshot dataSnapshot = task.getResult();

                    long count = dataSnapshot.getChildrenCount();
                    Toast.makeText(UpdateUser.this, " getting data! have " + count, Toast.LENGTH_SHORT).show();

//                    long USER_ID = count+1;
//
//                    Toast.makeText(UpdateUser.this, " now!"+USER_ID, Toast.LENGTH_SHORT).show();

//                    Map<String, User> users = new HashMap<>();
//                    users.put(""+USER_ID, new User("name","password","phone","email"));

//                    mDatabase.setValue(users);

//                    String userId_Rec = dataSnapshot.child("userId").getValue(String.class);
                    long userId = Long.parseLong(UserID);
                    String email = Email.getText().toString();
                    String password = Password.getText().toString();
                    String phone = Phone.getText().toString();
                    String name = Name.getText().toString();

                    User user = new User(userId, name,password,phone,email);

//                    mDatabase.child("User").child(""+USER_ID).setValue(USER_ID);
                    mDatabase.child("User").child("User"+UserID).setValue(user);

                    Intent intent = new Intent(UpdateUser.this, Home.class);
                    intent.putExtra(USERID, UserID);
                    intent.putExtra(USER_NAME, name);
                    intent.putExtra(USER_EMAIL, email);
                    startActivity(intent);

                } else {
                    Log.e("FirebaseData", "Error getting data", task.getException());
                    Toast.makeText(UpdateUser.this, "Error getting data!", Toast.LENGTH_SHORT).show();

                }
            }
        });

//        if(){//check email enter not null
//            if("database get email != this email"){//check vaild email
//

////        mDatabase.child("users").child(userId).setValue(user);

//        Intent intent = new Intent(Register.this, Home.class);
//        startActivity(intent);

//            }else{//email exist
//                Toast.makeText(Register.this, "Error Register!", Toast.LENGTH_SHORT).show();
//            }
//        }else{//not entering email
//            Toast.makeText(Register.this, "Please enter value!", Toast.LENGTH_SHORT).show();
//        }

    }

    public void Back(View view){
        Intent intent = new Intent(UpdateUser.this, Home.class);
        intent.putExtra(USERID, UserID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }

}