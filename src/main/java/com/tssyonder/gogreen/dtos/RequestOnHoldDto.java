package com.tssyonder.gogreen.dtos;

import javax.validation.constraints.NotNull;


public class RequestOnHoldDto {

    @NotNull(message = "Required")
    private Long id;

    @NotNull(message = "Required")
    private Long dateRequestAccepted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateRequestAccepted() {
        return dateRequestAccepted;
    }

    public void setDateRequestAccepted(Long dateRequestAccepted) {
        this.dateRequestAccepted = dateRequestAccepted;
    }
}
