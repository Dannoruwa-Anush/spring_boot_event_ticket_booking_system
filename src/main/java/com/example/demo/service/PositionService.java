package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.requestDTO.PositionRequestDTO;
import com.example.demo.dto.responseDTO.PositionResponseDTO;

public interface PositionService {
    PositionResponseDTO createPosition(PositionRequestDTO positionRequestDTO);
    List<PositionResponseDTO> getAllPositions();
    PositionResponseDTO getPositionById(Long id);
    PositionResponseDTO updatePosition(Long id, PositionRequestDTO positionRequestDTO);
    void deletePosition(Long id);
}
