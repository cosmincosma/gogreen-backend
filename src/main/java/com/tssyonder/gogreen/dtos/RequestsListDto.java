package com.tssyonder.gogreen.dtos;

import com.tssyonder.gogreen.entities.Status;

import java.util.Date;

public class RequestsListDto {

    private Long id;
    private Date dateRequestCreated;
    private String materialName;
    private String quantity;
    private String unit;
    private Status status;
    private Date dateRequestAccepted;
    private String companyName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateRequestCreated() {
        return dateRequestCreated;
    }

    public void setDateRequestCreated(Date dateRequestCreated) {
        this.dateRequestCreated = dateRequestCreated;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDateRequestAccepted() {
        return dateRequestAccepted;
    }

    public void setDateRequestAccepted(Date dateRequestAccepted) {
        this.dateRequestAccepted = dateRequestAccepted;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
