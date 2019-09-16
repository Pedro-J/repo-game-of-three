package com.challenge.gameofthreeplayer.event.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

import static java.lang.String.format;

public abstract class JSONReceiver<T extends Serializable> implements Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONReceiver.class);

    private Class<T> tClass;

    private Integer number;

    private ObjectMapper jsonMapper = new ObjectMapper();

    public JSONReceiver(Integer number, Class<T> tClass) {
        this.number = number;
        this.tClass = tClass;
    }

    public void processMessage(String message) {
        try {
            LOGGER.info(getName() + " received: " + message);
            T event = jsonMapper.readValue(message, tClass);
            process(event);
        } catch (IOException ex) {
            LOGGER.error(format("M=processMessage;state=error;details=%s;message=Error on processing message: %s",
                    ex.getMessage(), message));
        } catch (Exception ex) {
            LOGGER.error(format("M=processMessage;state=error;details=%s;message=Unexpected error on processing: %s",
                    ex.getMessage(), message));

        }
    }

    protected String getName() {
        return "player_" + number;
    }

    protected abstract void process(T event);


}
