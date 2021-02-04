package com.tssyonder.gogreen.dtos;

import com.tssyonder.gogreen.entities.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "Required")
    @Size(max = 50, message = "Email does not respect the field validation rules")
    @Pattern(regexp = "([a-zA-Z0-9]+)[@]([a-z]+)[.]([a-z]+)", message = "Email does not respect the field validation rules")
    private String email;

    @NotNull(message = "Required")
    @Size(min = 8, max = 50, message = "Password does not respect the field validation rules")
    private String password;
    private Role role;


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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
