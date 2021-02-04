package com.tssyonder.gogreen.dtos;

import com.tssyonder.gogreen.entities.Role;

public class CitizenDetailsDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String citizenPhoneNumber;
    private String citizenAddress;
    private String latitude;
    private String longitude;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCitizenPhoneNumber() {
        return citizenPhoneNumber;
    }

    public void setCitizenPhoneNumber(String citizenPhoneNumber) {
        this.citizenPhoneNumber = citizenPhoneNumber;
    }

    public String getCitizenAddress() {
        return citizenAddress;
    }

    public void setCitizenAddress(String citizenAddress) {
        this.citizenAddress = citizenAddress;
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
}
