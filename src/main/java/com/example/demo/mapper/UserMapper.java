package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.UserRequestDTO;
import com.example.demo.dto.responseDTO.UserResponseDTO;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mustChangePassword", ignore = true)
    @Mapping(target = "passwordChangedAt", ignore = true)
    @Mapping(target = "systemAccount", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "accountLockedUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "customer", ignore = true)
    User toEntity(UserRequestDTO dto);


    UserResponseDTO toResponseDTO(User user);
    List<UserResponseDTO> toResponseDTOList(List<User> users);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mustChangePassword", ignore = true)
    @Mapping(target = "passwordChangedAt", ignore = true)
    @Mapping(target = "systemAccount", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "accountLockedUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateUserFromDto(UserRequestDTO dto, @MappingTarget User user);
}