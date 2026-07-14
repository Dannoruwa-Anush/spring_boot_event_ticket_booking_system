package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetailsServiceImpl;
import com.example.demo.security.jwt.JwtUtils;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
            CustomUserDetailsServiceImpl userDetailsService,
            JwtUtils jwtUtils, AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

            String token = jwtUtils.generateToken(userDetails);

            User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            logger.info("User logged in successfully. User ID: {}, Email: {}", user.getId(), user.getEmail());

            return authMapper.toLoginResponse(token, user);
        } catch (AuthenticationException ex) {
            logger.warn("Failed login attempt for email: {}", loginRequestDTO.getEmail());
            throw ex;
        }
    }
}
