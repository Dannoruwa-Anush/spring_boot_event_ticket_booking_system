package com.example.demo.dto.responseDTO;

import java.math.BigDecimal;

import com.example.demo.config.enums.SeatStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeatResponseDTO {
    private Long id;
    private String seatNumber;
    private String section;
    private BigDecimal price;
    private SeatStatusEnum status;
}
