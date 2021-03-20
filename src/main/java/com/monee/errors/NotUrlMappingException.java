package com.monee.errors;

public class NotUrlMappingException extends RuntimeException {

    public NotUrlMappingException(String message) {
        super(message);
    }

    public NotUrlMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
