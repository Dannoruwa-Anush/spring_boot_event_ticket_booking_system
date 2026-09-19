package com.example.demo.dto.requestDTO;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PositionPermissionRequestDTO {

    @NotNull(message = "Position ID cannot be null")
    private Long positionId;

    @NotEmpty(message = "Permission IDs cannot be empty")
    private List<Long> permissionIds;
}
