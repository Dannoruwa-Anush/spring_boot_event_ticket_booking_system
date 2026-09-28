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
import com.example.demo.dto.requestDTO.SeatRequestDTO;
import com.example.demo.entity.Seat;
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
    public EventResponseDTO createEvent(EventRequestDTO dto, MultipartFile posterImage) {

        // Image is required when creating an event
        if (posterImage == null || posterImage.isEmpty()) {
            throw new IllegalArgumentException("Poster image is required");
        }

        String imageFilename = fileStorageService.save(posterImage);

        try {
            // Convert DTO -> Event
            Event event = mapper.toEntity(dto);

            event.setPosterImage(imageFilename);

            // Create seats
            if (dto.getSeats() != null) {

                for (SeatRequestDTO seatDto : dto.getSeats()) {

                    Seat seat = new Seat();

                    seat.setSeatNumber(seatDto.getSeatNumber());
                    seat.setSection(seatDto.getSection());
                    seat.setPrice(seatDto.getPrice());
                    seat.setStatus(seatDto.getStatus());

                    // Set the parent Event
                    seat.setEvent(event);

                    // Add seat to Event
                    event.getSeats().add(seat);
                }
            }

            Event savedEvent = repository.save(event);

            logger.info("Event created successfully. ID: {}", savedEvent.getId());

            return mapper.toResponseDTO(savedEvent);

        } catch (Exception e) {

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

            // ----------------------------------------
            // 1. Update normal event fields
            // ----------------------------------------
            mapper.updateEntity(dto, event);


            // ----------------------------------------
            // 2. Update seats
            // ----------------------------------------
            updateSeats(event, dto.getSeats());


            // ----------------------------------------
            // 3. Handle new poster image
            // ----------------------------------------
            if (posterImage != null && !posterImage.isEmpty()) {

                // Save new image
                newImage = fileStorageService.save(posterImage);

                // Replace old image reference
                event.setPosterImage(newImage);
            }


            // ----------------------------------------
            // 4. Save event + seats
            // ----------------------------------------
            Event updatedEvent = repository.save(event);


            // ----------------------------------------
            // 5. Delete old image only after
            //    database save succeeds
            // ----------------------------------------
            if (newImage != null
                    && oldImage != null
                    && !oldImage.equals(newImage)) {

                fileStorageService.delete(oldImage);
            }


            logger.info(
                    "Event updated successfully. ID: {}",
                    updatedEvent.getId());

            return mapper.toResponseDTO(updatedEvent);

        } catch (Exception e) {

            // If database update fails,
            // remove newly uploaded image.
            if (newImage != null) {
                fileStorageService.delete(newImage);
            }

            throw e;
        }
    }

    // Helper Method
    private void updateSeats(Event event, List<SeatRequestDTO> seatDtos) {

        // If seats are not supplied,
        // don't change existing seats.
        if (seatDtos == null) {
            return;
        }


        // ----------------------------------------
        // 1. Update existing seats / add new seats
        // ----------------------------------------

        for (SeatRequestDTO seatDto : seatDtos) {

            Seat existingSeat = event.getSeats()
                    .stream()
                    .filter(seat ->
                            seat.getSeatNumber()
                                    .equals(seatDto.getSeatNumber()))
                    .findFirst()
                    .orElse(null);


            if (existingSeat != null) {

                // Existing seat -> update
                existingSeat.setSection(
                        seatDto.getSection());

                existingSeat.setPrice(
                        seatDto.getPrice());

                existingSeat.setStatus(
                        seatDto.getStatus());

            } else {

                // New seat -> create
                Seat newSeat = new Seat();

                newSeat.setSeatNumber(
                        seatDto.getSeatNumber());

                newSeat.setSection(
                        seatDto.getSection());

                newSeat.setPrice(
                        seatDto.getPrice());

                newSeat.setStatus(
                        seatDto.getStatus());

                // Set relationship
                newSeat.setEvent(event);

                // Add to event
                event.getSeats().add(newSeat);
            }
        }


        // ----------------------------------------
        // 2. Remove seats that are no longer
        //    included in the request
        // ----------------------------------------

        event.getSeats().removeIf(existingSeat ->
                seatDtos.stream()
                        .noneMatch(seatDto ->
                                seatDto.getSeatNumber()
                                        .equals(existingSeat.getSeatNumber()))
        );
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
        String imageFilename = event.getPosterImage();

        try {
            repository.delete(event);

            // Delete physical poster image
            if (imageFilename != null && !imageFilename.isBlank()) {

                fileStorageService.delete(imageFilename);
            }


            logger.info(
                    "Event and associated seats deleted successfully. ID: {}",
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
