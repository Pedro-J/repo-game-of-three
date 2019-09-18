package com.challenge.gameofthree.exception.handler;

import com.challenge.gameofthree.exception.HttpGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleUnexpectedException(Exception ex, WebRequest request){
        LOGGER.error(format("M=processMessage;state=error;type=%s;message=%s", ex.getClass(), ex.getMessage()));

        return new ResponseEntity<>("Unexpected error. Please contact the system's administration.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpGenericException.class})
    public ResponseEntity<Object> handleExpectedException(HttpGenericException ex, WebRequest request){
        LOGGER.error(format("M=processMessage;state=error;type=%s;message=%s", ex.getClass(), ex.getMessage()));
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

}
