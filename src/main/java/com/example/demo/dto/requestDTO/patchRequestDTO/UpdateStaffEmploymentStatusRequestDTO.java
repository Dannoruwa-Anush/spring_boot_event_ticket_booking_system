package com.example.demo.dto.requestDTO.patchRequestDTO;

import java.time.LocalDate;

import com.example.demo.config.enums.StaffEmploymentStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateStaffEmploymentStatusRequestDTO {
    private LocalDate termination_date;
    private StaffEmploymentStatusEnum employmentStatus;
}