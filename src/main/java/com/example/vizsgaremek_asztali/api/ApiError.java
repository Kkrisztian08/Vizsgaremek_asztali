package com.example.vizsgaremek_asztali.api;

public class ApiError {
    private String message;
    private int statusCode;

    public ApiError(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
