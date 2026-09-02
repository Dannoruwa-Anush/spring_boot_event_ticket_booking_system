package com.example.demo.dto.requestDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaffRequestDTO {
    private String nic;
    private String phoneNo;
    private LocalDate hireDate;

    private long positionId;
    private StaffUserRegisterRequestDTO user;
}
