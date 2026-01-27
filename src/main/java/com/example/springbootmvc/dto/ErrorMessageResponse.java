package com.example.springbootmvc.dto;

import java.time.LocalDateTime;

public class ErrorMessageResponse {
    private String message;
    private String detail;
    private LocalDateTime dateTime;

    public ErrorMessageResponse(String message, String detail, LocalDateTime dateTime) {
        this.message = message;
        this.detail = detail;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }
    public String getDetail() {
        return detail;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
