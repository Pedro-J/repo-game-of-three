package com.challenge.gameofthree.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpGenericException {

    public BadRequestException(String message){
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
