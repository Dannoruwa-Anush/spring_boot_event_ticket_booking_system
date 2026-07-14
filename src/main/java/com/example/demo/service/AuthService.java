package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.dto.responseDTO.LoginResponseDTO;

@Service
public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
