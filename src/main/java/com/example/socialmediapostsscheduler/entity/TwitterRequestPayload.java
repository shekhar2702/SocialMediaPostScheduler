package com.example.socialmediapostsscheduler.entity;

public class TwitterRequestPayload {
    String text;

    public TwitterRequestPayload(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
