package com.example.fixinventori.Chat.utils;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_COLLECTION_MANAGERS = "managers";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatappPreference";
    public static final String KEY_IS_SIGNED_IN= "isSignedIn";
    public static final String KEY_USER_ID= "userId";
    public static final String KEY_MANAGER_ID= "managerId";
    public static final String KEY_IMAGE= "image";
    public static final String KEY_FCM_TOKEN= "fcmToken";
    public static final String KEY_USER= "user";
    public static final String KEY_MANAGER= "manager";
    public static final String KEY_COLLECTION_CHAT= "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MESSAGE_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MESSAGE_DATA = "data";
    public static final String REMOTE_MESSAGE_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String > getRemoteMsgHeaders(){
        if(remoteMsgHeaders==null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MESSAGE_AUTHORIZATION, "key=AAAAPGQ--hY:APA91bGwBbcZ9k8fVTEjy7Uj228RAkwYDSsRShneP0mthej4-AM6SbuMfwzq5rBFsKuHmPds0Xg89A0u5sfkBaYQ5smZT5QX7xYk3huI_40C4cKOyEZNcIAtfhWklH_IlQq8cZqusFjW");
        }
        remoteMsgHeaders.put(REMOTE_MESSAGE_CONTENT_TYPE, "application/json");
        return remoteMsgHeaders;
    }
}
