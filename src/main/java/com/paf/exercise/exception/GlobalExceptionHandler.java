package com.paf.exercise.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        log.error("Resource Not Found Exception: ", exception);
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(new Date())
                .messages(singletonList(exception.getMessage()))
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<?> invalidInputException(InvalidInputException exception, WebRequest request) {
        log.error("Invalid Input Exception: ", exception);
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(new Date())
                .messages(singletonList(exception.getMessage()))
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        log.error("Exception: ", exception);
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(new Date())
                .messages(singletonList(exception.getMessage()))
                .build();
        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("Method Argument Not Valid Exception: ", exception);
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(toList());

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .messages(singletonList(exception.getMessage()))
                .messages(errors).build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(HttpServletRequest request,
                                                                     ConstraintViolationException exception) {
        log.error("Constraint Violation Exception: ", exception);
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(new Date())
                .messages(singletonList(exception.getMessage()))
                .path(request.getServletPath()).build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }
}
