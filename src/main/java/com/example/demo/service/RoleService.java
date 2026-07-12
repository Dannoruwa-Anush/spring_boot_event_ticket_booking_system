package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.RoleRequestDTO;
import com.example.demo.dto.responseDTO.RoleResponseDTO;

@Service
public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO getRoleById(Long id);
    RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO);
    void deleteRole(Long id);
}
