package com.example.demo.dto.responseDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaffResponseDTO {
    private Long id;
    private String employeeNo; 
    private String nic;
    private String phoneNo;
    private LocalDate hire_date;

    private UserResponseDTO user;
    private PositionResponseDTO position;
}
