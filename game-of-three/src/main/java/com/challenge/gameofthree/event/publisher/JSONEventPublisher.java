package com.challenge.gameofthree.event.publisher;

import com.challenge.gameofthree.exception.EventPublishExpcetion;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public abstract class JSONEventPublisher <T extends Serializable> implements EventPublisher<T> {

    private ObjectMapper mapper;

    public JSONEventPublisher() {
        this.mapper = new ObjectMapper();
    }

    protected void publish(String channel, T event) throws EventPublishExpcetion {
        try {
            String jsonEvent = mapper.writeValueAsString(event);
            publish(channel, jsonEvent);
        } catch (Exception e) {
            throw new EventPublishExpcetion(e);
        }

    }

    abstract void publish(String channel, String event) throws EventPublishExpcetion;
}
