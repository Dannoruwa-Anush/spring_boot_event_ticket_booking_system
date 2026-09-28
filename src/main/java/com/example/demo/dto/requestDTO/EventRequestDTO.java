package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // needed for JPA
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
}
