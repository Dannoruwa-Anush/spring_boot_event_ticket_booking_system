package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.PermissionRequestDTO;
import com.example.demo.dto.responseDTO.PermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.repository.PermissionRepository;

@Service
public class PermissionServiceImpl implements PermissionService{

    private final PermissionRepository repository;
    private final PermissionMapper mapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PermissionResponseDTO createPermission(PermissionRequestDTO permissionRequestDTO) {
        Permission permission = mapper.toEnity(permissionRequestDTO);
        Permission saved = repository.save(permission);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public PageResponseDTO<PermissionResponseDTO> getAllPermissions(Pageable pageable) {
        Page<Permission> permissions = repository.findAll(pageable);
        List<PermissionResponseDTO> content = mapper.toResponseDTOList(permissions.getContent());
        
        return new PageResponseDTO<>(
                content,
                permissions.getNumber(),
                permissions.getSize(),
                permissions.getTotalElements(),
                permissions.getTotalPages(),
                permissions.isFirst(),
                permissions.isLast());
    }

    @Override
    public PermissionResponseDTO getPermissionById(Long id) {
        Permission permission = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permission not found"));

        return mapper.toResponseDTO(permission);
    }

    @Override
    public PermissionResponseDTO updatePermission(Long id, PermissionRequestDTO permissionRequestDTO) {
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        mapper.updatePermissionFromDto(permissionRequestDTO, permission);

        Permission updated = repository.save(permission);

        logger.info("Permission updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deletePermission(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Permission is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Permission deleted successfully. ID: {}", id);
    }
}