package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.PositionRequestDTO;
import com.example.demo.dto.responseDTO.PositionResponseDTO;
import com.example.demo.entity.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "staff", ignore = true)
    Position toEnity(PositionRequestDTO dto);

    PositionResponseDTO toResponseDTO(Position position);
    List<PositionResponseDTO> toResponseDTOList(List<Position> positions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "staff", ignore = true)
    void updatePositionFromDto(PositionRequestDTO dto, @MappingTarget Position position);
}