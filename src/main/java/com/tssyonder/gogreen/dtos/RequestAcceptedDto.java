package com.tssyonder.gogreen.dtos;

import com.tssyonder.gogreen.entities.Status;

import javax.validation.constraints.NotNull;

public class RequestAcceptedDto {
    @NotNull(message = "Required")
    private Long id;

    @NotNull(message = "Required")
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
