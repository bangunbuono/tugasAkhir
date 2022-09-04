package com.example.fixinventori.Chat.Activity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fixinventori.Activity.User.UserSession;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingServices extends FirebaseMessagingService {
    UserSession session;
    String user;
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "onNewToken: " + token);
        session = new UserSession(this);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM", "onMessageReceived: " + message.getNotification().getBody());
    }
}
