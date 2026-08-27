package com.contactmanagement.backend;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.security.JwtService;

class JwtServiceTest {

    @Test
    void generateAndReadTokenSuccessfully() {

        JwtService jwtService = new JwtService();

        String secret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901"
                        .getBytes(StandardCharsets.UTF_8)
        );

        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);

        jwtService.initializeSigningKey();

        User user = new User();
        user.setId(10);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals(10, jwtService.getUserIdFromToken(token));
    }
}
