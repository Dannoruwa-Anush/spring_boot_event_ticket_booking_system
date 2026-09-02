package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.PermissionRequestDTO;
import com.example.demo.dto.responseDTO.PermissionResponseDTO;
import com.example.demo.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    Permission toEnity(PermissionRequestDTO dto);

    PermissionResponseDTO toResponseDTO(Permission permission);
    List<PermissionResponseDTO> toResponseDTOList(List<Permission> permissions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    void updatePermissionFromDto(PermissionRequestDTO dto, @MappingTarget Permission permission);
}
