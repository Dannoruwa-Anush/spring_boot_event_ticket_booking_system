package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.requestDTO.RolePermissionRequestDTO;
import com.example.demo.dto.responseDTO.RolePermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.RolePermission;
import com.example.demo.mapper.RolePermissionMapper;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

        private final RolePermissionRepository repository;
        private final RoleRepository roleRepository;
        private final PermissionRepository permissionRepository;
        private final RolePermissionMapper mapper;

        // Logger for auditing purposes
        private static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

        public RolePermissionServiceImpl(RolePermissionRepository repository, RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionMapper mapper) {
                this.repository = repository;
                this.roleRepository = roleRepository;
                this.permissionRepository = permissionRepository;
                this.mapper = mapper;
        }

        @Override
        @Transactional
        public void createRolePermissions(RolePermissionRequestDTO dto) {
                Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() ->new RuntimeException("Role not found with id: " + dto.getRoleId()));
                List<RolePermission> rolePermissions = new ArrayList<>();

                for (Long permissionId : dto.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId).orElseThrow(() ->new RuntimeException("Permission not found with id: " + permissionId));

                if (repository.existsByRoleIdAndPermissionId(dto.getRoleId(),permissionId)) {
                        throw new RuntimeException("Permission " + permissionId + " is already assigned to role " + dto.getRoleId());
                }

                RolePermission rolePermission = new RolePermission();

                rolePermission.setRole(role);
                rolePermission.setPermission(permission);

                rolePermissions.add(rolePermission);
                }

                repository.saveAll(rolePermissions);

                logger.info("Permissions assigned successfully to role. Role ID: {}",dto.getRoleId());
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponseDTO<RolePermissionResponseDTO> getAllRolePermissions(Pageable pageable) {
                Page<RolePermission> rolePermissions = repository.findAll(pageable);
                List<RolePermissionResponseDTO> content = mapper.toResponseDTOList(rolePermissions.getContent());

                return new PageResponseDTO<>(
                        content,
                        rolePermissions.getNumber(),
                        rolePermissions.getSize(),
                        rolePermissions.getTotalElements(),
                        rolePermissions.getTotalPages(),
                        rolePermissions.isFirst(),
                        rolePermissions.isLast());
        }

        @Override
        @Transactional(readOnly = true)
        public RolePermissionResponseDTO getRolePermissionById(Long id) {
                RolePermission rolePermission = repository.findById(id).orElseThrow(() -> new RuntimeException("RolePermission not found with id: " + id));
                return mapper.toResponseDTO(rolePermission);
        }

        @Override
        @Transactional
        public RolePermissionResponseDTO updateRolePermission(Long id, RolePermissionRequestDTO dto) {
                RolePermission rolePermission = repository.findById(id).orElseThrow(() ->new RuntimeException( "RolePermission not found with id: " + id));

                Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found with id: " + dto.getRoleId()));

                /*
                * One RolePermission record represents one
                * Role + Permission relationship.
                *
                * Therefore, update requires exactly one permission ID.
                */
                if (dto.getPermissionIds() == null || dto.getPermissionIds().size() != 1) {
                        throw new IllegalArgumentException("Update requires exactly one permission ID");
                }

                Long permissionId = dto.getPermissionIds().get(0);

                Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

                /*
                * Check whether another RolePermission already
                * contains the same Role + Permission combination.
                */
                boolean duplicateExists =
                        repository.existsByRoleIdAndPermissionId(
                                dto.getRoleId(),
                                permissionId);

                boolean sameRelationship =
                        rolePermission.getRole().getId().equals(dto.getRoleId())
                                && rolePermission.getPermission().getId().equals(permissionId);

                if (duplicateExists && !sameRelationship) {
                throw new RuntimeException(
                        "Permission " + permissionId
                                + " is already assigned to role "
                                + dto.getRoleId());
                }

                rolePermission.setRole(role);
                rolePermission.setPermission(permission);

                RolePermission updated = repository.save(rolePermission);

                logger.info(
                        "RolePermission updated successfully. ID: {}",
                        updated.getId());

                return mapper.toResponseDTO(updated);
        }

        @Override
        @Transactional
        public void deleteRolePermission(Long id) {

                if (!repository.existsById(id)) {
                throw new IllegalArgumentException(
                        "RolePermission is not found with id: " + id);
                }

                repository.deleteById(id);

                logger.info(
                        "RolePermission deleted successfully. ID: {}",
                        id);
        }
}
