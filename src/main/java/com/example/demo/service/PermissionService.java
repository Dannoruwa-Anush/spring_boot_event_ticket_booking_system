package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.responseDTO.PermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface PermissionService {
    PageResponseDTO<PermissionResponseDTO> getAllPermissions(Pageable pageable);
    PermissionResponseDTO getPermissionById(Long id);
}
