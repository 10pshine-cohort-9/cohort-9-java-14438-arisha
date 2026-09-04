package com.contactmanagement.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.dto.AuthResponse;
import com.contactmanagement.backend.dto.ChangePasswordRequest;
import com.contactmanagement.backend.dto.LoginRequest;
import com.contactmanagement.backend.dto.RegisterRequest;
import com.contactmanagement.backend.dto.UpdateProfileRequest;
import com.contactmanagement.backend.dto.UserProfileResponse;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.security.JwtService;
import com.contactmanagement.backend.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

            User user = authService.login(request);
            String token = jwtService.generateToken(user);

            return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @GetMapping("/profile")
    public UserProfileResponse getProfile( @AuthenticationPrincipal User user) {
        return new UserProfileResponse(
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber()
        );
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(@AuthenticationPrincipal User user, @Valid @RequestBody UpdateProfileRequest request) {
        User updatedUser = authService.updateProfile(user, request);

        return new UserProfileResponse(updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getPhoneNumber());
    }

    @PostMapping("/change-password")
    public void changePassword(@AuthenticationPrincipal User user,@Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(user, request);
    }
    
}
