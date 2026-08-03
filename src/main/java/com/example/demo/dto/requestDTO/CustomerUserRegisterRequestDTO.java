package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerUserRegisterRequestDTO {
    private String name;
    private String email;
    private String password;
}
