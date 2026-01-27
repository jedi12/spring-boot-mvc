package com.example.springbootmvc.controller;

import com.example.springbootmvc.dto.ErrorMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("REST: Ошибка валидации: {}", detail);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse("Ошибка валидации", detail, LocalDateTime.now()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(NoSuchElementException e) {
        String detail = e.getMessage();
        log.error("REST: Ошибка на сервере: {}", detail);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponse("Ошибка на сервере", detail, LocalDateTime.now()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        String detail = e.getMessage();
        log.error("REST: Ошибка на сервере: {}", detail);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse("Ошибка на сервере", detail, LocalDateTime.now()));
    }
}
