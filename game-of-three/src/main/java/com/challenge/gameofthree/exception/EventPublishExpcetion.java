package com.challenge.gameofthree.exception;

public class EventPublishExpcetion extends RuntimeException{

    public EventPublishExpcetion() {
    }

    public EventPublishExpcetion(String message) {
        super(message);
    }

    public EventPublishExpcetion(Throwable cause) {
        super(cause);
    }
}
