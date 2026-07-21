package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.requestDTO.LoginRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.dto.responseDTO.LoginResponseDTO;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService service;

    public AuthController(AuthService service) {

        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/register-customer")
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO response = service.registerCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
