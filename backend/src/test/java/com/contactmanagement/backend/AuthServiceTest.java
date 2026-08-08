package com.contactmanagement.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.contactmanagement.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.InjectMocks;
import com.contactmanagement.backend.service.AuthService;
import com.contactmanagement.backend.dto.RegisterRequest;
import static org.mockito.Mockito.when;
import com.contactmanagement.backend.entity.User;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks 
    private AuthService authService;

    //1. User Registration Test
    @Test 
    void registerUserSuccessfully(){
        // registration request with email only
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", null, "password123");

        // if database does not contain email
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        // When password is entered, hash version is returned
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        // repository would store registered user
        User savedUser = new User("Test User", "test@example.com", null, "hashedPassword");

        //saved user will be returned by repository
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.register(request);
        assertNotNull(result);

        // Test values
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("hashedPassword", result.getPasswordHash());

        verify(userRepository).save(any(User.class));
    }
    
}
