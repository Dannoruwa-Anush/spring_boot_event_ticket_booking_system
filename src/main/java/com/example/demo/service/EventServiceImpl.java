package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.requestDTO.EventRequestDTO;
import com.example.demo.dto.responseDTO.EventResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Event;
import com.example.demo.mapper.EventMapper;
import com.example.demo.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger =
            LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository repository;
    private final EventMapper mapper;
    private final FileStorageService fileStorageService;

    public EventServiceImpl(
            EventRepository repository,
            EventMapper mapper,
            FileStorageService fileStorageService) {

        this.repository = repository;
        this.mapper = mapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public EventResponseDTO createEvent(
            EventRequestDTO dto,
            MultipartFile posterImage) {

        // Image is required when creating an event
        if (posterImage == null || posterImage.isEmpty()) {
            throw new IllegalArgumentException(
                    "Poster image is required");
        }

        // Save image
        String imageFilename =
                fileStorageService.save(posterImage);

        try {
            // Convert DTO -> Entity
            Event event = mapper.toEntity(dto);

            // Set saved image filename
            event.setPosterImage(imageFilename);

            // Save event
            Event savedEvent = repository.save(event);

            logger.info(
                    "Event created successfully. ID: {}",
                    savedEvent.getId());

            return mapper.toResponseDTO(savedEvent);

        } catch (Exception e) {

            // Database save failed,
            // so remove the image that was already uploaded.
            fileStorageService.delete(imageFilename);

            throw e;
        }
    }

    @Override
    public PageResponseDTO<EventResponseDTO> getAllEvents(
            Pageable pageable) {

        Page<Event> events =
                repository.findAll(pageable);

        List<EventResponseDTO> content =
                mapper.toResponseDTOList(events.getContent());

        return new PageResponseDTO<>(
                content,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast()
        );
    }

    @Override
    public EventResponseDTO getEventById(Long id) {

        Event event = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Event not found with id: " + id));

        return mapper.toResponseDTO(event);
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(
            Long id,
            EventRequestDTO dto,
            MultipartFile posterImage) {

        // Find existing event
        Event event = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Event not found with id: " + id));

        // Keep reference to the old image
        String oldImage = event.getPosterImage();

        // Will contain the new image filename if uploaded
        String newImage = null;

        try {

            // Update normal event fields
            mapper.updateEntity(dto, event);

            // Handle new image
            if (posterImage != null && !posterImage.isEmpty()) {

                // Save new image
                newImage = fileStorageService.save(posterImage);

                // Replace old image reference
                event.setPosterImage(newImage);
            }

            // Save updated event
            Event updatedEvent = repository.save(event);

            // Delete old image ONLY after DB save succeeds
            if (newImage != null && oldImage != null && !oldImage.equals(newImage)) {
                fileStorageService.delete(oldImage);
            }

            logger.info("Event updated successfully. ID: {}", updatedEvent.getId());

            return mapper.toResponseDTO(updatedEvent);

        } catch (Exception e) {
            if (newImage != null) {
                fileStorageService.delete(newImage);
            }

            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {

        // Find event first
        Event event = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Event not found with id: " + id));

        // Get image filename before deleting entity
        String imageFilename =
                event.getPosterImage();

        try {

            // Delete event from database
            repository.delete(event);

            // Delete physical image
            if (imageFilename != null
                    && !imageFilename.isBlank()) {

                fileStorageService.delete(imageFilename);
            }

            logger.info(
                    "Event deleted successfully. ID: {}",
                    id);

        } catch (Exception e) {

            logger.error(
                    "Failed to delete event. ID: {}",
                    id,
                    e);

            throw e;
        }
    }
}
