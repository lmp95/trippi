package com.example.trippi;

public class Message {
    public String sender;
    public String message;
    public String timestamp;

    public Message() {
    }

    public Message(String sender, String message, String timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
}
