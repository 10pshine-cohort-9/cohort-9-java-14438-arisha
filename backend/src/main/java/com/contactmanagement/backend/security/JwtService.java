package com.contactmanagement.backend.security;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.contactmanagement.backend.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expiration);

        return Jwts.builder()
            .subject(String.valueOf(user.getId()))
            .issuedAt(issuedAt)
            .expiration(expiresAt)
            .signWith(getSigningKey())
            .compact();
    }

    public Integer getUserIdFromToken(String token) {

        Claims claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return Integer.valueOf(claims.getSubject());
    }

    public boolean isTokenValid(String token) {

        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
