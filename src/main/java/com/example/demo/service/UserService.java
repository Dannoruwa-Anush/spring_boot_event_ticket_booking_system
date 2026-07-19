package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.UserRequestDTO;
import com.example.demo.dto.responseDTO.UserResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    PageResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);

    UserResponseDTO findUserByEmail(String email);
}
