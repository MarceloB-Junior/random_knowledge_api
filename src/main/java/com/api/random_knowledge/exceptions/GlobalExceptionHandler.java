package com.api.random_knowledge.exceptions;

import com.api.random_knowledge.dtos.responses.ExceptionsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ExceptionsResponse> handleCategoryAlreadyExists(CategoryAlreadyExistsException e){
        var exception = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionsResponse> handleCategoryNotFound(CategoryNotFoundException e){
        var exception = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CuriosityAlreadyExistsException.class)
    public ResponseEntity<ExceptionsResponse> handleCuriosityAlreadyExists(CuriosityAlreadyExistsException e){
        var exception = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CuriosityNotFoundException.class)
    public ResponseEntity<ExceptionsResponse> handleCuriosityNotFound(CuriosityNotFoundException e){
        var exception = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionsResponse> handleUserAlreadyExists(UserAlreadyExistsException e){
        var exception = ExceptionsResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }
}
