package com.example.demo.dto.requestDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRequestDTO {
    private String address;
    private LocalDate date_of_birth;
    private String phoneNo;
    private UserRegisterRequestDTO user;
}
