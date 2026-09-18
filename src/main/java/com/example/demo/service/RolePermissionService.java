package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.RolePermissionRequestDTO;
import com.example.demo.dto.responseDTO.RolePermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface RolePermissionService {
    List<RolePermissionResponseDTO> createRolePermissions(RolePermissionRequestDTO dto);
    PageResponseDTO<RolePermissionResponseDTO> getAllRolePermissions(Pageable pageable);
    RolePermissionResponseDTO getRolePermissionById(Long id);
    RolePermissionResponseDTO updateRolePermission(Long id, RolePermissionRequestDTO dto);
    void deleteRolePermission(Long id);
}
