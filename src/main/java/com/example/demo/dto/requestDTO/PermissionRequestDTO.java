package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Getter 
@Setter
public class PermissionRequestDTO {
    private String name;
    private String description;
}
