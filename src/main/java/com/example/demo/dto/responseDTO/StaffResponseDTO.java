package com.example.demo.dto.responseDTO;

import java.time.LocalDate;

import com.example.demo.config.enums.StaffEmploymentStatusEnum;

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
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private StaffEmploymentStatusEnum employmentStatus;

    private UserResponseDTO user;
    private PositionResponseDTO position;
}
