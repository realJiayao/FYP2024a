package com.example.fyp2024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffLogin extends AppCompatActivity {
    public static final String STAFF_USERID = "com.example.fyp2024.STAFF_USERID";
    public static final String STAFF_USER_NAME = "com.example.fyp2024.STAFF_USER_NAME";
    public static final String STAFF_USER_EMAIL = "com.example.fyp2024.STAFF_USER_EMAIL";

    EditText Email;
    TextView Password;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
    }

    public void Login(View view){
        database = FirebaseDatabase.getInstance().getReference("Staff");//change  staff**********************

        EditText Email = (EditText) findViewById(R.id.Staff_Login_email_Input);
        TextView Password = (EditText) findViewById(R.id.Staff_Login_password_Input);

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
                            Intent intent = new Intent(StaffLogin.this, StaffHome.class);
                            intent.putExtra(STAFF_USERID, userId_rec);
                            intent.putExtra(STAFF_USER_NAME, name_rec);
                            intent.putExtra(STAFF_USER_EMAIL, email_rec);
                            startActivity(intent);

                        }else{
                            Toast.makeText(StaffLogin.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                        }
//                        System.out.println(user);
                    }
                } else {
                    Toast.makeText(StaffLogin.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error getting data: " + databaseError.getMessage());
            }
        });

    }

public void UserClient(View view){
    Intent intent = new Intent(StaffLogin.this, Login.class);
    startActivity(intent);

}


}