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
}
