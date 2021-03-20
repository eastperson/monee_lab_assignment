package com.monee.errors;

public class NotControllerException extends RuntimeException{

    public NotControllerException(String message) {
        super(message);
    }

    public NotControllerException(String message, Throwable cause) {
        super(message, cause);
    }

}
