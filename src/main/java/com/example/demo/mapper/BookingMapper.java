package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.BookingRequestDTO;
import com.example.demo.dto.responseDTO.BookingResponseDTO;
import com.example.demo.entity.Booking;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomerMapper.class,
        BookingSeatMapper.class,
        PaymentMapper.class
    }
)
public interface BookingMapper {

    // Entity -> Response DTO
    BookingResponseDTO toResponseDTO(Booking booking);

    // Entity List -> Response DTO List
    List<BookingResponseDTO> toResponseDTOList(List<Booking> bookings);

    // Request DTO -> Entity
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Booking toEntity(BookingRequestDTO requestDTO);

    // Update existing Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    @Mapping(target = "payment", ignore = true)
    void updateBookingFromDto(BookingRequestDTO requestDTO, @MappingTarget Booking booking);
}
