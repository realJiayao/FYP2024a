package com.example.fyp2024.DB;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fyp2024.Home;
import com.example.fyp2024.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationReceiverForPayment extends BroadcastReceiver {
    String channelId = "payment_channel_id";
    String channelName = "Payment_Channel";

    int user_id;

    @Override
    public void onReceive(Context context, Intent intent) {
        user_id = intent.getIntExtra("user_id", 0);

        Intent activityIntent = new Intent(context, Login.class);

        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Ticket expired!")
                .setContentText("Your ticket has been expired.Thank you for choose us.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your ticket has been expired.Thank you for choose us."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        try {
            notificationManager.notify(user_id, builder.build());
            Remove_paymentTicket();
        Log.d("NotificationReceiverPayment","Has permission");
        } catch (SecurityException e) {
            Log.d("NotificationReceiverPayment","No permission");
        }

    }

    public void Remove_paymentTicket() {//not required
        DatabaseReference paymentDatabase = FirebaseDatabase.getInstance().getReference("Payment");
        Log.d("Payment", "Data delete " + user_id);

        paymentDatabase.orderByChild("userId")
                .equalTo(Long.valueOf(user_id)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Payment", "Data with userId " + user_id + " deleted successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Payment", "Error deleting data with userId " + user_id + ": " + e.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Payment", "Error querying data: " + databaseError.getMessage());
                    }
                });
    }

}

