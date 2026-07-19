package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.UserRequestDTO;
import com.example.demo.dto.responseDTO.UserResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, UserMapper mapper,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        User user = mapper.toEntity(userRequestDTO);

        Role role = roleRepository.findById(userRequestDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);

        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        User savedUser = repository.save(user);

        return mapper.toResponseDTO(savedUser);
    }

    @Override
    public PageResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable) {

        Page<User> users = repository.findAll(pageable);

        List<UserResponseDTO> content = mapper.toResponseDTOList(users.getContent());

        return new PageResponseDTO<>(
                content,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        mapper.updateUserFromDto(userRequestDTO, user);

        User updated = repository.save(user);

        logger.info("User updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);

        logger.info("User deleted successfully. ID: {}", id);
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.toResponseDTO(user);
    }
}
