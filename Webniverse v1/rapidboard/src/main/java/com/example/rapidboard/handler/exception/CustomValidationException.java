package com.example.rapidboard.handler.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException(String message) {
        super(message);
    }
}
