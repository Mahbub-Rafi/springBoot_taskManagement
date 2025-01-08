package com.example.springBoot_taskManagement.controller.auth;

import com.example.springBoot_taskManagement.dto.AuthenticationRequest;
import com.example.springBoot_taskManagement.dto.AuthenticationResponse;
import com.example.springBoot_taskManagement.dto.SignupRequest;
import com.example.springBoot_taskManagement.dto.UserDto;
import com.example.springBoot_taskManagement.services.auth.AuthService;
import com.example.springBoot_taskManagement.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with this email");
        }

        UserDto createdUserDto = authService.signUpUser(signupRequest);
        if (createdUserDto == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User could not be created");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String jwtToken = jwtUtil.generateToken(authService.loadUserByEmail(authenticationRequest.getEmail()));

        AuthenticationResponse response = new AuthenticationResponse();
        response.setJwt(jwtToken);
        response.setUserId(authService.getUserIdByEmail(authenticationRequest.getEmail()));
        response.setUserRole(authService.getUserRoleByEmail(authenticationRequest.getEmail()));

        return ResponseEntity.ok(response);
    }
}
