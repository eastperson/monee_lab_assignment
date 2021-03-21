package com.monee.errors;

public class ThreadException extends RuntimeException{

    public ThreadException(String message) {
        super(message);
    }

    public ThreadException(String message, Throwable cause) {
        super(message, cause);
    }
}
