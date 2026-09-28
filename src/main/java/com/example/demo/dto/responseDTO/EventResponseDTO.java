package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String venue;
    private int capacity;
    private String posterImage;
    private String trailerUrl;
    private EventStatusEnum status;

    private StaffResponseDTO staff;
    private List<SeatResponseDTO> seats = new ArrayList<>();
}
