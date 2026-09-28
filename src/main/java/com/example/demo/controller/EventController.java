package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.requestDTO.EventRequestDTO;
import com.example.demo.dto.responseDTO.EventResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDTO> createEvent(
            @RequestPart("event") EventRequestDTO dto,
            @RequestPart("posterImage") MultipartFile posterImage) {

        EventResponseDTO response =
                service.createEvent(dto, posterImage);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<EventResponseDTO>> getAllEvents(
            Pageable pageable) {

        PageResponseDTO<EventResponseDTO> response =
                service.getAllEvents(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable Long id) {

        EventResponseDTO response =
                service.getEventById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping(
        value = "/{id}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @RequestPart("event") EventRequestDTO dto,
            @RequestPart(value = "posterImage", required = false)
            MultipartFile posterImage) {

        EventResponseDTO response =
                service.updateEvent(id, dto, posterImage);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id) {

        service.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }
}
