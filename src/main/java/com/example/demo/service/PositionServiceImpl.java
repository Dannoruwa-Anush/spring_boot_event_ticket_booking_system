package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.requestDTO.PositionRequestDTO;
import com.example.demo.dto.responseDTO.PositionResponseDTO;
import com.example.demo.entity.Position;
import com.example.demo.mapper.PositionMapper;
import com.example.demo.repository.PositionRepository;

public class PositionServiceImpl implements PositionService {

    private final PositionRepository repository;
    private final PositionMapper mapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);

    public PositionServiceImpl(PositionRepository repository, PositionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PositionResponseDTO createPosition(PositionRequestDTO positionRequestDTO) {
        Position position = mapper.toEnity(positionRequestDTO);
        Position saved = repository.save(position);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public List<PositionResponseDTO> getAllPositions() {
        List<Position> positions = repository.findAll();
        return mapper.toResponseDTOList(positions);
    }

    @Override
    public PositionResponseDTO getPositionById(Long id) {
        Position position = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Position not found"));

        return mapper.toResponseDTO(position);
    }

    @Override
    public PositionResponseDTO updatePosition(Long id, PositionRequestDTO positionRequestDTO) {
        Position position = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        mapper.updatePositionFromDto(positionRequestDTO, position);

        Position updated = repository.save(position);

        logger.info("Position updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deletePosition(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Position is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Position deleted successfully. ID: {}", id);
    }
}
