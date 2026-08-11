package com.contactmanagement.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanagement.backend.dto.AuthResponse;
import com.contactmanagement.backend.dto.ChangePasswordRequest;
import com.contactmanagement.backend.dto.LoginRequest;
import com.contactmanagement.backend.dto.RegisterRequest;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.security.JwtService;
import com.contactmanagement.backend.service.AuthService;


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
    public AuthResponse login(@RequestBody LoginRequest request) {

            User user = authService.login(request);
            String token = jwtService.generateToken(user);

            return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        User user = authService.register(request);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @PostMapping("/change-password")
    public void changePassword(
        @AuthenticationPrincipal User user,
        @RequestBody ChangePasswordRequest request) {

        authService.changePassword(user, request);
    }
    
}
