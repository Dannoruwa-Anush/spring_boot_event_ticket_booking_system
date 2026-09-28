package com.example.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.PaymentRequestDTO;
import com.example.demo.dto.responseDTO.PaymentResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;

@Service
public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PageResponseDTO<PaymentResponseDTO> getAllPayments(Pageable pageable);
    PaymentResponseDTO getPaymentById(Long id);
    PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO paymentRequestDTO);
    void deletePayment(Long id);
}
