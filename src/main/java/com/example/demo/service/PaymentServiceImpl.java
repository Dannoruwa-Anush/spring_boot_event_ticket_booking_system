package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.enums.PaymentStatusEnum;
import com.example.demo.config.enums.SeatStatusEnum;
import com.example.demo.dto.requestDTO.PaymentRequestDTO;
import com.example.demo.dto.responseDTO.PaymentResponseDTO;
import com.example.demo.dto.responseDTO.common.PageResponseDTO;
import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingSeat;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Seat;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PaymentRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper mapper;

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(
            PaymentRepository repository,
            BookingRepository bookingRepository,
            PaymentMapper mapper) {

        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @Override
    public PaymentResponseDTO createPayment(
            PaymentRequestDTO paymentRequestDTO) {

        // Convert DTO to Payment
        Payment payment =
                mapper.toEntity(paymentRequestDTO);

        // Find Booking
        Booking booking =
                bookingRepository
                        .findById(
                                paymentRequestDTO.getBookingId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found with id: "
                                                + paymentRequestDTO
                                                        .getBookingId()));

        // Set Booking
        payment.setBooking(booking);

        // Update seat statuses according to payment status
        updateSeatStatuses(
                booking,
                paymentRequestDTO.getStatus());

        // Save Payment
        Payment saved =
                repository.save(payment);

        logger.info(
                "Payment created successfully. ID: {}",
                saved.getId());

        return mapper.toResponseDTO(saved);
    }


    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PaymentResponseDTO> getAllPayments(
            Pageable pageable) {

        Page<Payment> payments =
                repository.findAll(pageable);

        List<PaymentResponseDTO> content =
                mapper.toResponseDTOList(
                        payments.getContent());

        return new PageResponseDTO<>(
                content,
                payments.getNumber(),
                payments.getSize(),
                payments.getTotalElements(),
                payments.getTotalPages(),
                payments.isFirst(),
                payments.isLast());
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long id) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        return mapper.toResponseDTO(payment);
    }


    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    @Override
    public PaymentResponseDTO updatePayment(
            Long id,
            PaymentRequestDTO paymentRequestDTO) {

        Payment payment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        // Update normal Payment fields
        mapper.updatePaymentFromDto(
                paymentRequestDTO,
                payment);

        // If bookingId is provided, update Booking
        if (paymentRequestDTO.getBookingId() != null) {

            Booking booking =
                    bookingRepository
                            .findById(
                                    paymentRequestDTO
                                            .getBookingId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Booking not found with id: "
                                                    + paymentRequestDTO
                                                            .getBookingId()));

            payment.setBooking(booking);
        }

        // Update seat statuses
        if (paymentRequestDTO.getStatus() != null) {

            updateSeatStatuses(
                    payment.getBooking(),
                    paymentRequestDTO.getStatus());
        }

        Payment updated =
                repository.save(payment);

        logger.info(
                "Payment updated successfully. ID: {}",
                updated.getId());

        return mapper.toResponseDTO(updated);
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @Override
    public void deletePayment(Long id) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Payment is not found with id: " + id);
        }

        repository.deleteById(id);

        logger.info(
                "Payment deleted successfully. ID: {}",
                id);
    }


    // =========================================================
    // UPDATE SEAT STATUS
    // =========================================================

    private void updateSeatStatuses(
            Booking booking,
            PaymentStatusEnum paymentStatus) {

        if (booking == null) {
            throw new RuntimeException(
                    "Booking is required to update seat status");
        }

        if (paymentStatus == null) {
            return;
        }

        SeatStatusEnum seatStatus =
                getSeatStatus(paymentStatus);

        // Some payment statuses don't change seat status
        if (seatStatus == null) {
            return;
        }

        for (BookingSeat bookingSeat :
                booking.getBookingSeats()) {

            Seat seat = bookingSeat.getSeat();

            if (seat == null) {
                continue;
            }

            seat.setStatus(seatStatus);
        }
    }


    // =========================================================
    // PAYMENT STATUS -> SEAT STATUS
    // =========================================================

    private SeatStatusEnum getSeatStatus(
            PaymentStatusEnum paymentStatus) {

        return switch (paymentStatus) {

            case PENDING ->
                    SeatStatusEnum.RESERVED;

            case COMPLETED ->
                    SeatStatusEnum.BOOKED;

            case FAILED,
                 CANCELLED,
                 REFUNDED ->
                    SeatStatusEnum.AVAILABLE;
        };
    }
}
