package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RolePermissionRequestDTO {
    private Long RoleId;
    
    @NotEmpty(message = "Permission IDs cannot be empty")
    private List<Long> permissionIds;
}
