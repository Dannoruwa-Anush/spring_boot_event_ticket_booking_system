package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.requestDTO.CustomerRequestDTO;
import com.example.demo.dto.responseDTO.CustomerResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserRepository;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository repository;
    private final EventMapper mapper;

    // Logger for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    public EventServiceImpl(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = mapper.toEntity(eventRequestDTO);
        Event saved = repository.save(event);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public PageResponseDTO<EventResponseDTO> getAllEvents(Pageable pageable) {
        Page<Event> events = repository.findAll(pageable);
        List<EventResponseDTO> content = mapper.toResponseDTOList(events.getContent());

        return new PageResponseDTO<>(
                content,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast());
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return mapper.toResponseDTO(event);
    }

    @Override
    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        mapper.updateEventFromDto(eventRequestDTO, event);

        Event updated = repository.save(event);

        logger.info("Event updated successfully. ID: {}", updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Event is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info("Event deleted successfully. ID: {}", id);
    }
}
