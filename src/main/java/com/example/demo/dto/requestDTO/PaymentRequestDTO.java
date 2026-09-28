package com.example.demo.dto.requestDTO;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private BigDecimal amount;
    private LocalDateTime paidAt;
    private String transactionRef;
    private PaymentMethodEnum paymentMethod;
    private PaymentStatusEnum status;
    private Long bookingId;
}
