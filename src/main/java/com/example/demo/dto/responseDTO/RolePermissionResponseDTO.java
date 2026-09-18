package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionResponseDTO {
    private Long id;
    private RoleResponseDTO role;
    private PermissionResponseDTO permission;
}
