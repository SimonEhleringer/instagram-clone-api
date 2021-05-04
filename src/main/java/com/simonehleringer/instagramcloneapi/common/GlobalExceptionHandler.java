package com.simonehleringer.instagramcloneapi.common;

import com.simonehleringer.instagramcloneapi.common.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // Return error response with all errors when controller level validation fails
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        var errorResponse = new ErrorResponse(new ArrayList<>());

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errorResponse.getErrors().add(error.getDefaultMessage());
        }

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    // Handle all unhandled exceptions
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Return error response with exception message
    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        ErrorResponse response = new ErrorResponse(new ArrayList<>());

        response.getErrors().add(ex.getMessage());

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }
}
