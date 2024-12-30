package com.example.springBoot_taskManagement.services.auth;

import com.example.springBoot_taskManagement.dto.SignupRequest;
import com.example.springBoot_taskManagement.dto.UserDto;

public interface AuthService {

    UserDto signUpUser(SignupRequest signupRequest);

    boolean hasUserWithEmail(String email);

}
