package com.contactmanagement.backend;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.contactmanagement.backend.dto.LoginRequest;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.repository.UserRepository;
import com.contactmanagement.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceLoginTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    //Case 1: User successfully logs in using email
    @Test
    void loginSuccessfullyWithEmail() {

        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User existingUser = new User("Test User", "test@example.com", null, "hashedPassword");

        // User is found by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        // Entered password matches stored password
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        User result = authService.login(request);

        // Test values
        assertNotNull(result);
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).findByEmail("test@example.com");
    }

    //Case 2: user successfully logs in using phone number
    @Test
    void loginSuccessfullyWithPhoneNumber() {

        LoginRequest request = new LoginRequest("03001234567", "password123");
        User existingUser = new User("Test User", null, "03001234567", "hashedPassword");

        when(userRepository.findByEmail("03001234567")).thenReturn(Optional.empty()); //email is found empty
        //User found by phone number
        when(userRepository.findByPhoneNumber("03001234567")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        User result = authService.login(request);

        // Test values
        assertNotNull(result);
        assertEquals("Test User", result.getFullName());
        assertEquals("03001234567", result.getPhoneNumber());

        // Check both searches happened
        verify(userRepository).findByEmail("03001234567");
        verify(userRepository).findByPhoneNumber("03001234567");
    }
}
