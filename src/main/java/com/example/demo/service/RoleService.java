package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.RoleRequestDTO;
import com.example.demo.dto.responseDTO.RoleResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
    PageResponseDTO<RoleResponseDTO> getAllRoles(Pageable pageable);
    RoleResponseDTO getRoleById(Long id);
    RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO);
    void deleteRole(Long id);
}
