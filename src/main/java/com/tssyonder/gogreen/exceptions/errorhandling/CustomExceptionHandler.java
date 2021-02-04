package com.tssyonder.gogreen.exceptions.errorhandling;


import com.tssyonder.gogreen.exceptions.BadRequestException;
import com.tssyonder.gogreen.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = Logger.getLogger(CustomExceptionHandler.class.getName());

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        logger.info("EXCEPTION: Bad Request");
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        logger.info("EXCEPTION: Not Found");
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> defaultHandler(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        logger.info("EXCEPTION: Internal Server Error");
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
