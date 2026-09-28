package com.example.demo.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponseDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime paidAt;
    private String transactionRef;
    private PaymentMethodEnum paymentMethod;
    private PaymentStatusEnum status;

    private BookingResponseDTO booking; 
}
