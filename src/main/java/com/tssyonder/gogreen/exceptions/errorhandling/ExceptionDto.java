package com.tssyonder.gogreen.exceptions.errorhandling;

import org.springframework.http.HttpStatus;

public class ExceptionDto {

    private String meesage;
    private HttpStatus httpStatus;


    public ExceptionDto() {
    }

    public ExceptionDto(String meesage, HttpStatus httpStatus) {
        this.meesage = meesage;
        this.httpStatus = httpStatus;
    }

    public String getMeesage() {
        return meesage;
    }

    public void setMeesage(String meesage) {
        this.meesage = meesage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
