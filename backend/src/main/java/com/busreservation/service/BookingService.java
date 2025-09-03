package com.busreservation.service;

import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.model.User;

import java.util.List;

public interface BookingService {
    Booking createBooking(HoldRequest request);        // used by controller in your errors
    Booking confirmBooking(Long bookingId);           // controller was calling confirmBooking(Long)
    Booking holdBooking(Long userId, Long tripId, double amount);
    Booking cancelBooking(Long bookingId, String reason);
    Booking getBooking(Long bookingId);               // needed by controller/getter usage
    List<Booking> getBookingsByUser(User user);
    List<Booking> getAllBookings();
    Booking updateBookingStatus(Long bookingId, String status);
}
