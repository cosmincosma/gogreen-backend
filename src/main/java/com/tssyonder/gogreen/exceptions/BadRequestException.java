package com.tssyonder.gogreen.exceptions;

public class BadRequestException extends RequestCustomException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
