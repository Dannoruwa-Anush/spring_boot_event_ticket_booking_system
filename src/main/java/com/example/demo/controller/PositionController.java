package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.requestDTO.PositionRequestDTO;
import com.example.demo.dto.responseDTO.PositionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController {
    
    private final PositionService service;

    public PositionController(PositionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PositionResponseDTO> createPosition(@RequestBody PositionRequestDTO dto) {
        PositionResponseDTO response = service.createPosition(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<PositionResponseDTO>> getAllPositions(Pageable pageable) {
        PageResponseDTO<PositionResponseDTO> response = service.getAllPositions(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> getPositionById(@PathVariable Long id) {
        PositionResponseDTO response = service.getPositionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> updatePosition(@PathVariable Long id, @RequestBody PositionRequestDTO dto) {
        PositionResponseDTO response = service.updatePosition(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        service.deletePosition(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
