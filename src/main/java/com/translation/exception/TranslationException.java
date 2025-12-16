package com.translation.exception;

import org.springframework.http.HttpStatus;

public class TranslationException extends RuntimeException {
    
    private final HttpStatus status;
    private final String errorCode;
    
    public TranslationException(String message, HttpStatus status) {
        this(message, status, "TRANSLATION_ERROR");
    }
    
    public TranslationException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
