package com.dynamo.sporter.model;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUserID;
    private String messageClubName;
    private long messageTime;

    public ChatMessage(String messageText, String messageUserID, String messageClubName) {
        this.messageText = messageText;
        this.messageUserID = messageUserID;
        this.messageClubName = messageClubName;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getMessageClubName() {
        return messageClubName;
    }
}
