package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.RoleRequestDTO;
import com.example.demo.dto.responseDTO.RoleResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final UserRepository userRepository;
    private final RoleMapper mapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    public RoleServiceImpl(RoleRepository repository, UserRepository userRepository, RoleMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        Role role = mapper.toEntity(roleRequestDTO);
        Role saved = repository.save(role);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = repository.findAll();
        return mapper.toResponseDTOList(roles);
    }

    @Override
    public RoleResponseDTO getRoleById(Long id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return mapper.toResponseDTO(role);
    }

    @Override
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        mapper.updateRoleFromDto(roleRequestDTO, role);

        Role updated = repository.save(role);

        logger.info("Role updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteRole(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Role is not found with id: " + id);
        }

        // Check if an user is associated with the role
        if (userRepository.existsByRoleId(id)) {
            throw new RuntimeException("Cannot delete role because it is assigned to a user.");
        }

        repository.deleteById(id);

        logger.info("Role deleted successfully. ID: {}", id);
    }
}
