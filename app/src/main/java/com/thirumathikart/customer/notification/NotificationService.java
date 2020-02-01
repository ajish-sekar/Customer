package com.thirumathikart.customer.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thirumathikart.customer.db.Notification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class NotificationService extends FirebaseMessagingService {

    public static String GENERAL_CHANNEL_ID = "General Channel";
    public static String GENERAL_CHANNEL_NAME = "General Notification";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
