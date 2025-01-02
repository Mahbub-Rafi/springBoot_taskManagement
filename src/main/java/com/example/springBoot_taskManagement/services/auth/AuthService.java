package com.example.springBoot_taskManagement.services.auth;

import com.example.springBoot_taskManagement.dto.SignupRequest;
import com.example.springBoot_taskManagement.dto.UserDto;
import com.example.springBoot_taskManagement.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    UserDto signUpUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);

    UserDetails loadUserByEmail(String email);

    Long getUserIdByEmail(String email);

    UserRole getUserRoleByEmail(String email);
}
