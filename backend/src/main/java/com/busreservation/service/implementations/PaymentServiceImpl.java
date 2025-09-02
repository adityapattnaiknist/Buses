package com.busreservation.service.implementations;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.busreservation.service.PaymentService;
import com.busreservation.repository.*;
import com.busreservation.model.*;
import com.busreservation.model.enums.PaymentStatus;
import java.time.LocalDateTime;
import com.busreservation.exception.ApiException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public String checkout(Long bookingId, Double amount) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ApiException("Booking not found"));
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .status(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // mark booking confirmed
        booking.setStatus(com.busreservation.model.enums.BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return "PAYMENT_SUCCESS";
    }

    @Override
    public String refund(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElseThrow(() -> new ApiException("Payment not found"));
        p.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(p);
        Booking booking = p.getBooking();
        if (booking != null) {
            booking.setStatus(com.busreservation.model.enums.BookingStatus.CANCELLED);
            booking.setCancelledAt(LocalDateTime.now());
            bookingRepository.save(booking);
        }
        return "REFUND_SUCCESS";
    }
}
