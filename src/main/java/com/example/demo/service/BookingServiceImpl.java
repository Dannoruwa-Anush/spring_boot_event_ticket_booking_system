package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.requestDTO.BookingRequestDTO;
import com.example.demo.dto.responseDTO.BookingResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingSeat;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SeatRepository;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final BookingMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(BookingServiceImpl.class);

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            CustomerRepository customerRepository,
            SeatRepository seatRepository,
            BookingMapper mapper) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.mapper = mapper;
    }

    @Override
    public BookingResponseDTO createBooking(
            BookingRequestDTO bookingRequestDTO) {

        // Convert simple DTO fields to Booking entity
        Booking booking =
                mapper.toEntity(bookingRequestDTO);

        // Find Customer
        Customer customer =
                customerRepository
                        .findById(
                                bookingRequestDTO.getCustomerId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found with id: "
                                        + bookingRequestDTO
                                                .getCustomerId()));

        booking.setCustomer(customer);

        // Create BookingSeat entities
        List<BookingSeat> bookingSeats =
                createBookingSeats(
                        bookingRequestDTO
                                .getBookingSeatsNumbers(),
                        booking);

        booking.setBookingSeats(bookingSeats);

        // Save Booking
        Booking saved =
                bookingRepository.save(booking);

        logger.info(
                "Booking created successfully. ID: {}",
                saved.getId());

        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<BookingResponseDTO> getAllBookings(
            Pageable pageable) {

        Page<Booking> bookings =
                bookingRepository.findAll(pageable);

        List<BookingResponseDTO> content =
                mapper.toResponseDTOList(
                        bookings.getContent());

        return new PageResponseDTO<>(
                content,
                bookings.getNumber(),
                bookings.getSize(),
                bookings.getTotalElements(),
                bookings.getTotalPages(),
                bookings.isFirst(),
                bookings.isLast());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {

        Booking booking =
                bookingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found with id: "
                                        + id));

        return mapper.toResponseDTO(booking);
    }

    @Override
    public BookingResponseDTO updateBooking(
            Long id,
            BookingRequestDTO bookingRequestDTO) {

        Booking booking =
                bookingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found with id: "
                                        + id));

        // Update simple fields
        mapper.updateBookingFromDto(
                bookingRequestDTO,
                booking);

        // Update Customer
        if (bookingRequestDTO.getCustomerId() != null) {

            Customer customer =
                    customerRepository
                            .findById(
                                    bookingRequestDTO
                                            .getCustomerId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Customer not found with id: "
                                            + bookingRequestDTO
                                                    .getCustomerId()));

            booking.setCustomer(customer);
        }

        // Update Booking Seats
        if (bookingRequestDTO
                .getBookingSeatsNumbers() != null) {

            List<BookingSeat> bookingSeats =
                    createBookingSeats(
                            bookingRequestDTO
                                    .getBookingSeatsNumbers(),
                            booking);

            // orphanRemoval = true
            booking.getBookingSeats().clear();
            booking.getBookingSeats()
                    .addAll(bookingSeats);
        }

        // Save updated booking
        Booking updated =
                bookingRepository.save(booking);

        logger.info(
                "Booking updated successfully. ID: {}",
                updated.getId());

        return mapper.toResponseDTO(updated);
    }

    @Override
    public void deleteBooking(Long id) {

        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Booking is not found with id: " + id);
        }

        bookingRepository.deleteById(id);

        logger.info(
                "Booking deleted successfully. ID: {}",
                id);
    }

    private List<BookingSeat> createBookingSeats(
            List<String> seatNumbers,
            Booking booking) {

        List<BookingSeat> bookingSeats =
                new ArrayList<>();

        for (String seatNumber : seatNumbers) {

            Seat seat =
                    seatRepository
                            .findBySeatNumber(seatNumber)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Seat not found with number: "
                                            + seatNumber));

            BookingSeat bookingSeat =
                    new BookingSeat();

            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);

            bookingSeats.add(bookingSeat);
        }

        return bookingSeats;
    }
}
