package com.tssyonder.gogreen.exceptions;

public class NotFoundException extends RequestCustomException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
