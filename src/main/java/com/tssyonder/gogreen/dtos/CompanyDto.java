package com.tssyonder.gogreen.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class CompanyDto {

    @NotNull(message = "Required")
    @Size(max = 50, message = "Email does not respect the field validation rules")
    @Pattern(regexp = "([a-zA-Z0-9]+)[@]([a-z]+)[.]([a-z]+)", message = "Email does not respect the field validation rules")
    private String email;

    @NotNull(message = "Required")
    @Size(min = 8, max = 50, message = "Password does not respect the field validation rules")
    private String password;

    @NotNull(message = "Required")
    @Size(max = 30, message = "Name does not respect the field validation rules")
    private String companyName;

    @NotNull(message = "Required")
    @Size(max = 150, message = "Description does not respect the field validation rules")
    private String description;

    @NotNull(message = "Required")
    @Size(max = 30, message = "Street does not respect the field validation rules")
    @Pattern(message = "Street does not respect the field validation rules", regexp = "[a-zA-Z ]*$")
    private String street;

    @NotNull(message = "Required")
    @Size(max = 10, message = "Number does not respect the field validation rules")
    @Pattern(message = "Number does not respect the field validation rules", regexp = "[0-9]*$")
    private String number;

    @NotNull(message = "Required")
    @Size(max = 10, message = "Building does not respect the field validation rules")
    @Pattern(message = "Building does not respect the field validation rules", regexp = "[a-zA-Z0-9-]*$")
    private String building;

    @NotNull(message = "Required")
    @Size(max = 10, message = "Entrance does not respect the field validation rules")
    @Pattern(message = "Entrance does not respect the field validation rules", regexp = "[a-zA-Z0-9-]*$")
    private String entrance;

    @NotNull(message = "Required")
    @Size(max = 10, message = "Apartment number does not respect the field validation rules")
    @Pattern(message = "Apartment number does not respect the field validation rules", regexp = "[0-9-]*$")
    private String appNumber;

    @NotNull(message = "Required")
    @Size(min = 10, max = 10, message = "Phone number does not respect the field validation rules")
    @Pattern(message = "Phone number does not respect the field validation rules", regexp = "[0][7]([0-9]{8})*$")
    private String companyPhoneNumber;

    @NotNull(message = "Required")
    @Size(min = 4, max = 12, message = "UIC does not respect the field validation rules")
    @Pattern(message = "UIC does not respect the field validation rules", regexp = "[R][O]([0-9]+)*$")
    private String uniqueCode;

    @NotNull(message = "Required")
    @Size(min = 1, max = 15, message = "Types of waste does not respect the validation rules")
    private List<
            @Pattern(message = "Material name does not respect the field validation rules", regexp = "[a-zA-Z- ]*$")
            @Size(min = 1, max = 20, message = "Material name does not respect the field validation rules")
                    String> materialName;

    private String latitude;
    private String longitude;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getMaterialName() {
        return materialName;
    }

    public void setMaterialName(List<String> materialName) {
        this.materialName = materialName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }
}
