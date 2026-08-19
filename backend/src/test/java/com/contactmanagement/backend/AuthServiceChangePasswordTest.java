package com.contactmanagement.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    
}
