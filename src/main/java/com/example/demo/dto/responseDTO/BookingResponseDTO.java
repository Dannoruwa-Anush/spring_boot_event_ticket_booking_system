package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingResponseDTO {
    private Long id;
    private BigDecimal totalAmount;
    private LocalDate bookingDate;
    private BookingStatusEnum status;

    private CustomerResponseDTO customer;

    private List<BookingSeatResponseDTO> bookingSeats = new ArrayList<>();

    private PaymentResponseDTO payment;
}
