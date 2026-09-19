package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.PositionPermissionRequestDTO;
import com.example.demo.dto.responseDTO.PositionPermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface PositionPermissionService {
    List<PositionPermissionResponseDTO> createPositionPermissions(PositionPermissionRequestDTO dto);
    PageResponseDTO<PositionPermissionResponseDTO> getAllPositionPermissions(Pageable pageable);
    PositionPermissionResponseDTO getPositionPermissionById(Long id);
    PositionPermissionResponseDTO updatePositionPermission(Long id, PositionPermissionRequestDTO dto);
    void deletePositionPermission(Long id);
}
