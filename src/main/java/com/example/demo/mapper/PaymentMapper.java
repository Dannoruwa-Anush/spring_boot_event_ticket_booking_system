package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.PaymentRequestDTO;
import com.example.demo.dto.responseDTO.PaymentResponseDTO;
import com.example.demo.entity.Payment;

@Mapper(
    componentModel = "spring",
    uses = {
        BookingMapper.class
    }
)
public interface PaymentMapper {

    PaymentResponseDTO toResponseDTO(Payment payment);

    List<PaymentResponseDTO> toResponseDTOList(
            List<Payment> payments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    Payment toEntity(PaymentRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    void updatePaymentFromDto(
            PaymentRequestDTO requestDTO,
            @MappingTarget Payment payment);
}
