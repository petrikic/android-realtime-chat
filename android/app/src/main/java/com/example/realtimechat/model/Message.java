package com.example.realtimechat.model;

public class Message {

    private long messageId;
    private long userReferenceId;
    private long senderId;
    private String text;
    private long timestamp;

     public Message(String text, long userReferenceId, long senderId, long timestamp) {
         this.text = text;
         this.userReferenceId = userReferenceId;
         this.senderId = senderId;
         this.timestamp = timestamp;
     }

    public Message(long messageId, long userReferenceId, long senderId, String text, long timestamp) {
        this.messageId = messageId;
        this.userReferenceId = userReferenceId;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public long getMessageId() {
        return messageId;
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
