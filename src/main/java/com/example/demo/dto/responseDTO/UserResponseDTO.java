package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // needed for JPA
@AllArgsConstructor
@Data // getters & setters
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;

    private RoleResponseDTO role;
}
