package com.example.realtimechat.model;

public class Message {

    private String text;
    private long userReferenceId;
    private long senderId;
    private long timestamp;

     public Message(String text, long userReferenceId, long senderId, long timestamp) {
         this.text = text;
         this.senderId = senderId;
         this.timestamp = timestamp;
     }

    public String getText() {
        return text;
    }

    public long getUserReferenceId() {
        return userReferenceId;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
