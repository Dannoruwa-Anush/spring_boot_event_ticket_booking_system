package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.requestDTO.StaffRequestDTO;
import com.example.demo.dto.responseDTO.StaffResponseDTO;

public interface StaffService {
    StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO);
    List<StaffResponseDTO> getAllStaffMembers();
    StaffResponseDTO getStaffById(Long id);
    StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO);
    void deleteStaff(Long id);
}
