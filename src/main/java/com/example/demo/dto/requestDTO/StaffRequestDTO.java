package com.example.demo.dto.requestDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaffRequestDTO {
    private String employeeNo; 
    private String nic;
    private String phoneNo;
    private LocalDate hire_date;

    private UserRequestDTO user;
    private long positionId;
}
