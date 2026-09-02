package com.example.demo.dto.responseDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponseDTO {
    private Long id;
    private String address;
    private LocalDate dateOfBirth;
    private String phoneNo;

    private UserResponseDTO user;
}
