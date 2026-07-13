package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.UserRequestDTO;
import com.example.demo.dto.responseDTO.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = mapper.toEntity(userRequestDTO);
        User saved = repository.save(user);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return mapper.toResponseDTOList(users);
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

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.toResponseDTO(user);
    }
}
