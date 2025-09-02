package com.busreservation.service.implementations;

import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.model.User;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.UserRepository;
import com.busreservation.repository.TripRepository;
import com.busreservation.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository; // optional -- used if you want to link Trip
    private final UserRepository userRepository; // optional

    public BookingServiceImpl(BookingRepository bookingRepository,
                              TripRepository tripRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking createBooking(HoldRequest request) {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        booking.setAmount(request.getAmount());
        booking.setCreatedAt(LocalDateTime.now());

        // Set user/trip if repositories exist
        if (request.getUserId() != null && userRepository != null) {
            User user = userRepository.findById(request.getUserId()).orElse(null);
            booking.setUser(user);
        }
        if (request.getTripId() != null && tripRepository != null) {
            tripRepository.findById(request.getTripId()).ifPresent(booking::setTrip);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking holdBooking(Long userId, Long tripId, double amount) {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.HOLD);
        booking.setAmount(amount);
        booking.setCreatedAt(LocalDateTime.now());
        // optional: set user/trip if repos available
        if (userRepository != null && userId != null) {
            userRepository.findById(userId).ifPresent(booking::setUser);
        }
        if (tripRepository != null && tripId != null) {
            tripRepository.findById(tripId).ifPresent(booking::setTrip);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
    }
}
