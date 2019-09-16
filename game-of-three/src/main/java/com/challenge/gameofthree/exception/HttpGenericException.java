package com.challenge.gameofthree.exception;

import org.springframework.http.HttpStatus;

public abstract class HttpGenericException extends RuntimeException {

    public HttpGenericException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatusCode();
}
