package com.example.springBoot_taskManagement.services.auth;

import com.example.springBoot_taskManagement.dto.SignupRequest;
import com.example.springBoot_taskManagement.dto.UserDto;
import com.example.springBoot_taskManagement.entities.User;
import com.example.springBoot_taskManagement.enums.UserRole;
import com.example.springBoot_taskManagement.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void CreateAnAdminAccount() {
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin account created successfully!");
        } else {
            System.out.println("Admin account already exists!");
        }
    }

    @Override
    public UserDto signUpUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.EMPLOYEE);
        User createdUser = userRepository.save(user);
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .map(User::getId) // Retrieve the ID of the user
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    public UserRole getUserRoleByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .map(User::getUserRole) // Retrieve the role of the user
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}

