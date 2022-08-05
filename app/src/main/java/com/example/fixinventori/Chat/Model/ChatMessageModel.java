package com.example.fixinventori.Chat.Model;

import java.util.Date;

public class ChatMessageModel {
    public String senderId, receiverId, message, dateTime, conversionId, conversionName, conversionImage;
    public Date dateObject;

    public String getConversionName() {
        return conversionName;
    }
}
