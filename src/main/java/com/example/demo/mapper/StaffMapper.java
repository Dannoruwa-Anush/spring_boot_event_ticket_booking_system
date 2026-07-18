package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.StaffRequestDTO;
import com.example.demo.dto.responseDTO.StaffResponseDTO;
import com.example.demo.entity.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "termination_date", ignore = true)
    @Mapping(target = "employmentStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "position", ignore = true)
    Staff toEntity(StaffRequestDTO dto);

    StaffResponseDTO toResponseDTO(Staff staff);
    List<StaffResponseDTO> toResponseDTOList(List<Staff> staffMembers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "termination_date", ignore = true)
    @Mapping(target = "employmentStatus", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "position", ignore = true)
    void updateStaffFromDTO(StaffRequestDTO dto, @MappingTarget Staff staff);
}
