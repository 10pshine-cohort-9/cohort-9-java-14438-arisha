package com.contactmanagement.backend.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.contactmanagement.backend.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

import com.contactmanagement.backend.entity.User;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);

                    if (jwtService.isTokenValid(token)) {
                        Integer userId = jwtService.getUserIdFromToken(token);

                        User user = userRepository.findById(userId).orElse(null);

                        if (user != null) {
                            UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
                filterChain.doFilter(request, response);
            }
}
