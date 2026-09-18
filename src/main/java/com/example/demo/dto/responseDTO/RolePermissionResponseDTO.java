package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RolePermissionResponseDTO {
    private Long id;
    private RoleResponseDTO role;
    private PermissionResponseDTO permission;
}
