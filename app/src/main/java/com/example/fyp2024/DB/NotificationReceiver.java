package com.example.fyp2024.DB;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fyp2024.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationReceiver extends BroadcastReceiver {
    String channelId = "booking_channel_id";
    String channelName = "Booking_Channel";

    String message;
    String notification_facilityName;
    int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {


        message = intent.getStringExtra("notification_message");
        notification_facilityName = intent.getStringExtra("notification_facilityName");
        notificationId = intent.getIntExtra("notification_id", 0);
        String message_output = "Be ready for Your booking rides (" + notification_facilityName + ")! Time:" + message + ".";

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        Notification notification = new Notification.Builder(context, channelId)
//                .setContentTitle("Amusement Park Booking Notification")
//                .setContentText("This notification was scheduled to appear at this time.")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .build();
//
//        notificationManager.notify(100, notification);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Booking come up!")
                .setContentText(message_output)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message_output))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        try {
            notificationManager.notify(notificationId, builder.build());
//            Add_NotificationRecord();
            Log.d("NotificationReceiver","Has permission");
        } catch (SecurityException e) {
            Log.d("NotificationReceiver","No permission");
        }

    }

    public void Add_NotificationRecord() {//not required
        DatabaseReference notificationDatabase = FirebaseDatabase.getInstance().getReference("NotificationRecord");
        notificationDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    long Count_record = dataSnapshot.getChildrenCount();
                    Log.d("Notification Page", "Count_record!" + Count_record);

                    if (Count_record == 0) {
                        long notificationId = 0;

                        NotificationDB Notification_Record = new NotificationDB(notificationId, message, notification_facilityName);
                        notificationDatabase.child("Notification" + notificationId).setValue(Notification_Record);
                        Log.d("Notification Page", "NotificationId Check set up!");
                        Log.d("Notification Page", "NotificationId!"+notificationId);


                    } else {
                        notificationDatabase.orderByChild("notificationId")
                                .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Log.d("Notification Page", "Error getting Notification detail!");
                                        } else {
                                            for (DataSnapshot notificationSnapshot : dataSnapshot.getChildren()) {
                                                Object notificationId_object = notificationSnapshot.child("notificationId").getValue();
                                                String notificationId_rec = String.valueOf(notificationId_object);
                                                System.out.println("notificationId_rec " + notificationId_rec);
                                                long notificationId_long = Long.parseLong(notificationId_rec);

                                                long new_notificationId_long = notificationId_long + 1;
                                                Log.d("Notification Page", "new_notificationId_long!"+new_notificationId_long);


                                                NotificationDB Notification_Record = new NotificationDB(new_notificationId_long, message, notification_facilityName);
                                                notificationDatabase.child("Notification" + new_notificationId_long).setValue(Notification_Record);
                                                Log.d("Notification Page", "NotificationRecord set up!");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.err.println("Error getting data: " + databaseError.getMessage());
                                    }
                                });
                    }
                } else {
                    Log.e("Notification Page", "Error getting data", task.getException());
                }
            }
        });
    }

}

