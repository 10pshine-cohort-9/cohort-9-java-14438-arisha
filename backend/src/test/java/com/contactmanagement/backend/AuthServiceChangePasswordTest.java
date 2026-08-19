package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.contactmanagement.backend.dto.ChangePasswordRequest;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.repository.UserRepository;
import com.contactmanagement.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceChangePasswordTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void changePasswordSuccessfully() {
        User user = new User("Test User", "test@example.com", null, "oldHashedPassword");

        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword123");

        when(passwordEncoder.matches("oldPassword", "oldHashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newHashedPassword");

        authService.changePassword(user, request);
        assertEquals("newHashedPassword", user.getPasswordHash());
        verify(userRepository).save(user);
    }
    
}
