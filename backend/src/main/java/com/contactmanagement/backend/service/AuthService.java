package com.contactmanagement.backend.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contactmanagement.backend.dto.ChangePasswordRequest;
import com.contactmanagement.backend.dto.LoginRequest;
import com.contactmanagement.backend.dto.RegisterRequest;
import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.exception.InvalidCredentialsException;
import com.contactmanagement.backend.repository.UserRepository;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        String fullName = request.getFullName();
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        //User must provide atleast a email or a phone number.
        if ((email == null || email.isBlank()) && (phoneNumber == null || phoneNumber.isBlank())){
            throw new IllegalArgumentException("Email or phone number is required");
        }

        //Duplicate email check
        if (email != null && !email.isBlank()){ //if email is provided by the user
            if (userRepository.existsByEmail(email)){ //if email exists in database
                throw new IllegalArgumentException("Email is already registered");
            }
        }

        //Duplicate phone number check
        if (phoneNumber != null && !phoneNumber.isBlank()){ //if number is provided by the user
            if (userRepository.existsByPhoneNumber(phoneNumber)){ //if number exists in database
                throw new IllegalArgumentException("Phone number is already registered");
            }
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
            fullName,
            email,
            phoneNumber,
            encodedPassword
        );
        try {
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with ID: {}", savedUser.getId());
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Email or phone number already exists");
        }
    }

    public User login(LoginRequest request) {
        String identifier = request.getIdentifier();

        Optional<User> user = userRepository.findByEmail(identifier); //Try finding user by email

        if(user.isEmpty()){
            user = userRepository.findByPhoneNumber(identifier); //If can not find user by email, try phone number
        }
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");// can not find user by email or phone number
        }

        User foundUser = user.get();
        String enteredPassword = request.getPassword();
        String storedPasswordHash = foundUser.getPasswordHash();

        if (!passwordEncoder.matches(enteredPassword, storedPasswordHash)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return foundUser;
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        String currentPassword = request.getCurrentPassword();
        String storedPasswordHash = user.getPasswordHash();

        if (!passwordEncoder.matches(currentPassword, storedPasswordHash)) {
            throw new IllegalArgumentException("Current password is incorrect"); //if current password is incorrect
        }

        // if current password is correct
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        // update password
        user.setPasswordHash(newEncodedPassword);
        userRepository.save(user);

    }
}
