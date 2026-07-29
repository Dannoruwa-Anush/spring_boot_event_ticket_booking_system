package com.example.demo.dto.responseDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponseDTO {
    private long id;
    private String address;
    private LocalDate date_of_birth;
    private String phoneNo;

    private UserResponseDTO user;
}
