package com.example.iceamapp.entity;

public class ChatMessage {
    private String user;
    private String text;

    public ChatMessage(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
