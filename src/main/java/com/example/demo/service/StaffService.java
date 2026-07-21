package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.StaffRequestDTO;
import com.example.demo.dto.responseDTO.StaffResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface StaffService {
    StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO);
    PageResponseDTO<StaffResponseDTO> getAllStaffMembers(Pageable pageable);
    StaffResponseDTO getStaffById(Long id);
    StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO);
    void deleteStaff(Long id);
}
