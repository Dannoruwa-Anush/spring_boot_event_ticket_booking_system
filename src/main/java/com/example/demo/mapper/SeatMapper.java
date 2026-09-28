package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.responseDTO.SeatResponseDTO;
import com.example.demo.entity.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatResponseDTO toResponseDTO(Seat seat);
}
