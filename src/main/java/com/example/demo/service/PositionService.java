package com.example.demo.service;

import org.springframework.data.domain.Pageable;

import com.example.demo.dto.requestDTO.PositionRequestDTO;
import com.example.demo.dto.responseDTO.PositionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

public interface PositionService {
    PositionResponseDTO createPosition(PositionRequestDTO positionRequestDTO);
    PageResponseDTO<PositionResponseDTO> getAllPositions(Pageable pageable);
    PositionResponseDTO getPositionById(Long id);
    PositionResponseDTO updatePosition(Long id, PositionRequestDTO positionRequestDTO);
    void deletePosition(Long id);
}
