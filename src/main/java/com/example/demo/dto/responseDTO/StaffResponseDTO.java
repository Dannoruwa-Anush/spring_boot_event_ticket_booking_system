package com.example.demo.dto.responseDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaffResponseDTO {
    private long id;
    private String employeeNo; 
    private String nic;
    private String phoneNo;
    private LocalDateTime hire_date;

    private UserResponseDTO user;
    private PositionResponseDTO position;
}
