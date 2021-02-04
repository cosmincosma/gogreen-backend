package com.tssyonder.gogreen.dtos;

import com.tssyonder.gogreen.entities.Role;

import java.util.List;

public class CitizenNearbyDto {
    private Long id;
    private String name;
    private String latitude;
    private String longitude;
    private List<String> citizenRequestsMaterials;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<String> getCitizenRequestsMaterials() {
        return citizenRequestsMaterials;
    }

    public void setCitizenRequestsMaterials(List<String> citizenRequestsMaterials) {
        this.citizenRequestsMaterials = citizenRequestsMaterials;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
