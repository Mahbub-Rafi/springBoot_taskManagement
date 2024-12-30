package com.example.springBoot_taskManagement.dto;

import com.example.springBoot_taskManagement.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private int id;

    private String name;

    private String email;

    private String password;

    private UserRole userRole;

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
