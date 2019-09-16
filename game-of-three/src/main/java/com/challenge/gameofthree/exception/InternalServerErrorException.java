package com.challenge.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends HttpGenericException {

    public InternalServerErrorException(String message){
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
