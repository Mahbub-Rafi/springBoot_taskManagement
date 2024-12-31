package com.example.springBoot_taskManagement.dto;

import com.example.springBoot_taskManagement.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private int userId;

    private UserRole userRole;


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
