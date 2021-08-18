package com.example.firebaseapp.Models;

import com.google.firebase.database.PropertyName;

public class Modelchat {
    String message,sender,reciever,timestamp;
    boolean isseen;

    public Modelchat() {
    }

    public Modelchat(String message, String sender, String reciever, String timestamp, boolean isseen) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.timestamp = timestamp;
        this.isseen = isseen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @PropertyName("isSeen")
    public boolean isSeen() {
        return isseen;
    }
    @PropertyName("Is seen")
    public void setSeen(boolean seen) {
        isseen = seen;
    }

    public boolean isseen() {
        return isseen;
    }

    public void setseen(boolean isseen) {
        this.isseen = isseen;
    }
}
