package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.contactmanagement.backend.dto.RegisterRequest;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.repository.UserRepository;
import com.contactmanagement.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceRegistrationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks 
    private AuthService authService;

    //Case 1: User Registration Successful
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

    //Case 2: Email already exists in database and registration fails
    @Test
    void registerFailsWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", null, "password123");

        // email already exists in database
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    //Case 3: Phone number already exists in database and registration fails
    @Test
    void registerFailsWhenPhoneAlreadyExists() {

        RegisterRequest request = new RegisterRequest("Test User", null, "03001234567", "password123");

        // Phone number already exists in database
        when(userRepository.existsByPhoneNumber("03001234567")).thenReturn(true);

        assertThrows( IllegalArgumentException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }
    
}
