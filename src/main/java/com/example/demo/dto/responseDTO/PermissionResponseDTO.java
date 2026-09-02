package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Getter 
@Setter
public class PermissionResponseDTO {
    private Long id;
    private String name;
    private String description;
}
