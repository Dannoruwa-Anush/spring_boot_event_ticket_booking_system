package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.requestDTO.PositionPermissionRequestDTO;
import com.example.demo.dto.responseDTO.PositionPermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Position;
import com.example.demo.entity.PositionPermission;
import com.example.demo.mapper.PositionPermissionMapper;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.PositionPermissionRepository;
import com.example.demo.repository.PositionRepository;

@Service
public class PositionPermissionServiceImpl implements PositionPermissionService {

        private final PositionPermissionRepository repository;
        private final PositionRepository positionRepository;
        private final PermissionRepository permissionRepository;
        private final PositionPermissionMapper mapper;

        // Logger for auditing purposes
        private static final Logger logger = LoggerFactory.getLogger(PositionPermissionServiceImpl.class);

        public PositionPermissionServiceImpl(PositionPermissionRepository repository, PositionRepository positionRepository,
                        PermissionRepository permissionRepository, PositionPermissionMapper mapper) {
                this.repository = repository;
                this.positionRepository = positionRepository;
                this.permissionRepository = permissionRepository;
                this.mapper = mapper;
        }

        @Override
        @Transactional
        public List<PositionPermissionResponseDTO> createPositionPermissions(
                        PositionPermissionRequestDTO dto) {

                Position position = positionRepository.findById(dto.getPositionId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Position not found with id: " + dto.getPositionId()));

                List<PositionPermission> positionPermissions = new ArrayList<>();

                for (Long permissionId : dto.getPermissionIds()) {

                        Permission permission = permissionRepository.findById(permissionId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Permission not found with id: " + permissionId));

                        if (repository.existsByPositionIdAndPermissionId(
                                        dto.getPositionId(), permissionId)) {

                                throw new RuntimeException(
                                                "Permission " + permissionId +
                                                                " is already assigned to position " + dto.getPositionId());
                        }

                        PositionPermission positionPermission = new PositionPermission();

                        positionPermission.setPosition(position);
                        positionPermission.setPermission(permission);

                        positionPermissions.add(positionPermission);
                }

                List<PositionPermission> savedPositionPermissions = repository.saveAll(positionPermissions);

                logger.info(
                                "Permissions assigned successfully to position. Position ID: {}",
                                dto.getPositionId());

                return mapper.toResponseDTOList(savedPositionPermissions);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponseDTO<PositionPermissionResponseDTO> getAllPositionPermissions(Pageable pageable) {
                Page<PositionPermission> positionPermissions = repository.findAll(pageable);
                List<PositionPermissionResponseDTO> content = mapper.toResponseDTOList(positionPermissions.getContent());

                return new PageResponseDTO<>(
                                content,
                                positionPermissions.getNumber(),
                                positionPermissions.getSize(),
                                positionPermissions.getTotalElements(),
                                positionPermissions.getTotalPages(),
                                positionPermissions.isFirst(),
                                positionPermissions.isLast());
        }

        @Override
        @Transactional(readOnly = true)
        public PositionPermissionResponseDTO getPositionPermissionById(Long id) {
                PositionPermission positionPermission = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("PositionPermission not found with id: " + id));
                return mapper.toResponseDTO(positionPermission);
        }

        @Override
        @Transactional
        public PositionPermissionResponseDTO updatePositionPermission(Long id, PositionPermissionRequestDTO dto) {
                PositionPermission positionPermission = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("PositionPermission not found with id: " + id));

                Position position = positionRepository.findById(dto.getPositionId())
                                .orElseThrow(() -> new RuntimeException("Position not found with id: " + dto.getPositionId()));

                /*
                 * One PositionPermission record represents one
                 * Position + Permission relationship.
                 *
                 * Therefore, update requires exactly one permission ID.
                 */
                if (dto.getPermissionIds() == null || dto.getPermissionIds().size() != 1) {
                        throw new IllegalArgumentException("Update requires exactly one permission ID");
                }

                Long permissionId = dto.getPermissionIds().get(0);

                Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                                () -> new RuntimeException("Permission not found with id: " + permissionId));

                /*
                 * Check whether another PositionPermission already
                 * contains the same Position + Permission combination.
                 */
                boolean duplicateExists = repository.existsByPositionIdAndPermissionId(
                                dto.getPositionId(),
                                permissionId);

                boolean sameRelationship = positionPermission.getPosition().getId().equals(dto.getPositionId())
                                && positionPermission.getPermission().getId().equals(permissionId);

                if (duplicateExists && !sameRelationship) {
                        throw new RuntimeException(
                                        "Permission " + permissionId
                                                        + " is already assigned to position "
                                                        + dto.getPositionId());
                }

                positionPermission.setPosition(position);
                positionPermission.setPermission(permission);

                PositionPermission updated = repository.save(positionPermission);

                logger.info(
                                "PositionPermission updated successfully. ID: {}",
                                updated.getId());

                return mapper.toResponseDTO(updated);
        }

        @Override
        @Transactional
        public void deletePositionPermission(Long id) {

                if (!repository.existsById(id)) {
                        throw new IllegalArgumentException(
                                        "PositionPermission is not found with id: " + id);
                }

                repository.deleteById(id);

                logger.info(
                                "PositionPermission deleted successfully. ID: {}",
                                id);
        }
}
