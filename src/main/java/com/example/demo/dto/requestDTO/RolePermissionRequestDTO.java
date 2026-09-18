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
public class RolePermissionRequestDTO {

    @NotNull(message = "Role ID cannot be null")
    private Long roleId;

    @NotEmpty(message = "Permission IDs cannot be empty")
    private List<Long> permissionIds;
}
