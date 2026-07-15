package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.security.CustomUserDetailsImpl;
import com.example.demo.security.jwt.JwtUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthMapper authMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

         try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDTO.getEmail(),
                                    loginRequestDTO.getPassword()));

            CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();

            User user = userDetails.getUser();

            String token = jwtUtils.generateToken(userDetails);

            logger.info("User logged in successfully. User ID: {}, Email: {}", user.getId(), user.getEmail());

            return authMapper.toLoginResponse(token, user);

        } catch (AuthenticationException ex) {

            logger.warn("Failed login attempt for email: {}", loginRequestDTO.getEmail());

            throw ex;
        }
    }
}
