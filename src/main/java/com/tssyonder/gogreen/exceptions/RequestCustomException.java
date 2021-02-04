package com.tssyonder.gogreen.exceptions;

public class RequestCustomException extends RuntimeException {

    public RequestCustomException(String message) {
        super(message);
    }

    public RequestCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
