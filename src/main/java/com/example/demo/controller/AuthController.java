package com.example.demo.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.security.CustomUserDetailsServiceImpl;
import com.example.demo.security.jwt.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtService,
            CustomUserDetailsServiceImpl userDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        return jwtService.generateToken(userDetails);
    }
}
