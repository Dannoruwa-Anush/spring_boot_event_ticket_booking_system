package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.PermissionRequestDTO;
import com.example.demo.dto.responseDTO.PermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface PermissionService {
    PermissionResponseDTO createPermission(PermissionRequestDTO permissionRequestDTO);
    PageResponseDTO<PermissionResponseDTO> getAllPermissions(Pageable pageable);
    PermissionResponseDTO getPermissionById(Long id);
    PermissionResponseDTO updatePermission(Long id, PermissionRequestDTO permissionRequestDTO);
    void deletePermission(Long id);
}
