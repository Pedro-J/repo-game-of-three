package com.challenge.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpGenericException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

}
