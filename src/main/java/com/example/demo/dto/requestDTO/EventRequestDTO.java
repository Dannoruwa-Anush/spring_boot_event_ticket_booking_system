package com.example.demo.dto.requestDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.config.enums.EventStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventRequestDTO {
    private String title;
    private String description;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String venue;
    private int capacity;
    private String trailerUrl;
    private EventStatusEnum status;

    private List<SeatRequestDTO> seats;
}
