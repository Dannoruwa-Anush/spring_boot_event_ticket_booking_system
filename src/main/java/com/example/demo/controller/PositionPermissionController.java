package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.requestDTO.PositionPermissionRequestDTO;
import com.example.demo.dto.responseDTO.PositionPermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.service.PositionPermissionService;

@RestController
@RequestMapping("/position-permissions")
public class PositionPermissionController {
        private final PositionPermissionService service;

        public PositionPermissionController(PositionPermissionService service) {
                this.service = service;
        }

        @PostMapping
        public ResponseEntity<List<PositionPermissionResponseDTO>> createPositionPermissions(@Validated @RequestBody PositionPermissionRequestDTO dto) {
                List<PositionPermissionResponseDTO> response = service.createPositionPermissions(dto);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        
        @GetMapping
        public ResponseEntity<PageResponseDTO<PositionPermissionResponseDTO>> getAllPositionPermissions(Pageable pageable) {
                PageResponseDTO<PositionPermissionResponseDTO> response = service.getAllPositionPermissions(pageable);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<PositionPermissionResponseDTO> getPositionPermissionById(@PathVariable Long id) {
                PositionPermissionResponseDTO response = service.getPositionPermissionById(id);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<PositionPermissionResponseDTO> updatePositionPermission(@PathVariable Long id, @Validated @RequestBody PositionPermissionRequestDTO dto) {
                PositionPermissionResponseDTO response = service.updatePositionPermission(id, dto);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePositionPermission(@PathVariable Long id) {
                service.deletePositionPermission(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
}
