package com.example.demo.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingRequestDTO {
    private Long id;
    private BigDecimal totalAmount;
    private LocalDate bookingDate;
    private BookingStatusEnum status;

    private Long customerId;

    private List<String> bookingSeatsNumbers = new ArrayList<>();
}
