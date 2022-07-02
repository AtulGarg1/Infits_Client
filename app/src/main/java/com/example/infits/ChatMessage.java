package com.example.infits;

public class ChatMessage {
    public  String senderId, receiverId,message,time,messageBy,read;

    public ChatMessage(String senderId, String receiverId, String message, String time, String messageBy, String read) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.time = time;
        this.messageBy=messageBy;
        this.read=read;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getMessageBy() {
        return messageBy;
    }

    public String getRead() {
        return read;
    }
}
