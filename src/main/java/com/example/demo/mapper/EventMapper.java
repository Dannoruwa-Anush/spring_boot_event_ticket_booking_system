package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.requestDTO.EventRequestDTO;
import com.example.demo.dto.responseDTO.EventResponseDTO;
import com.example.demo.entity.Event;

@Mapper(
    componentModel = "spring",
    uses = {
        StaffMapper.class,
        SeatMapper.class
    }
)
public interface EventMapper {

    // EventRequestDTO -> Event
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "posterImage", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "seats", ignore = true)
    Event toEntity(EventRequestDTO dto);

    // Event -> EventResponseDTO
    EventResponseDTO toResponseDTO(Event event);

    // Update existing Event from EventRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "posterImage", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "seats", ignore = true)
    void updateEntity(
        EventRequestDTO dto,
        @MappingTarget Event event
    );

    // List mapping
    List<EventResponseDTO> toResponseDTOList(List<Event> events);
}
