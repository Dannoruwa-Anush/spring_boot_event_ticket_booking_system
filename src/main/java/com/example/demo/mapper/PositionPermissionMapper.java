package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.dto.responseDTO.PositionPermissionResponseDTO;
import com.example.demo.entity.PositionPermission;

@Mapper(
    componentModel = "spring",
    uses = {
        PositionMapper.class,
        PermissionMapper.class
    }
)
public interface PositionPermissionMapper {
    PositionPermissionResponseDTO toResponseDTO(PositionPermission positionPermission);
    List<PositionPermissionResponseDTO> toResponseDTOList(List<PositionPermission> positionPermissions);
}
