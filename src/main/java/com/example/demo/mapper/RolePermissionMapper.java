package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.dto.responseDTO.RolePermissionResponseDTO;
import com.example.demo.entity.RolePermission;

@Mapper(
    componentModel = "spring",
    uses = {
        RoleMapper.class,
        PermissionMapper.class
    }
)
public interface RolePermissionMapper {
    RolePermissionResponseDTO toResponseDTO(RolePermission rolePermission);
    List<RolePermissionResponseDTO> toResponseDTOList(List<RolePermission> rolePermissions);
}
