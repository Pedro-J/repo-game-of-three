package com.challenge.gameofthree.exception.handler;

import com.challenge.gameofthree.exception.HttpGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.lang.String.format;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request){
        LOGGER.error(format("M=processMessage;state=error;type=%s;message=%s", ex.getClass(), ex.getMessage()));

        return new ResponseEntity<>("Unexpected error. Please contact the system's administration.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpGenericException.class})
    public ResponseEntity<Object> handleNotFoundException(HttpGenericException ex, WebRequest request){
        LOGGER.error(format("M=processMessage;state=error;type=%s;message=%s", ex.getClass(), ex.getMessage()));
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

}
