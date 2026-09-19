package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionPermissionResponseDTO {
    private Long id;
    private PositionResponseDTO position;
    private PermissionResponseDTO permission;
}
