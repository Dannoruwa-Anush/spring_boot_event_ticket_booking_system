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

import com.example.demo.dto.requestDTO.RolePermissionRequestDTO;
import com.example.demo.dto.responseDTO.RolePermissionResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.service.RolePermissionService;

@RestController
@RequestMapping("/role-permissions")
public class RolePermissionController {
        private final RolePermissionService service;

        public RolePermissionController(RolePermissionService service) {
                this.service = service;
        }

        @PostMapping
        public ResponseEntity<List<RolePermissionResponseDTO>> createRolePermissions(@Validated @RequestBody RolePermissionRequestDTO dto) {
                List<RolePermissionResponseDTO> response = service.createRolePermissions(dto);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        
        @GetMapping
        public ResponseEntity<PageResponseDTO<RolePermissionResponseDTO>> getAllRolePermissions(Pageable pageable) {
                PageResponseDTO<RolePermissionResponseDTO> response = service.getAllRolePermissions(pageable);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<RolePermissionResponseDTO> getRolePermissionById(@PathVariable Long id) {
                RolePermissionResponseDTO response = service.getRolePermissionById(id);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<RolePermissionResponseDTO> updateRolePermission(@PathVariable Long id, @Validated @RequestBody RolePermissionRequestDTO dto) {
                RolePermissionResponseDTO response = service.updateRolePermission(id, dto);
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRolePermission(@PathVariable Long id) {
                service.deleteRolePermission(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
}
