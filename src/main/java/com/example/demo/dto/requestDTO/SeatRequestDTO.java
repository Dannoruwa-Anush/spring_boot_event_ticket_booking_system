package com.example.demo.dto.requestDTO;

import java.math.BigDecimal;

import com.example.demo.config.enums.SeatStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatRequestDTO {
    private String seatNumber;
    private String section;
    private BigDecimal price;
    private SeatStatusEnum status;
}
