package com.busreservation.controller;

import com.busreservation.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Hold/create booking
    @PostMapping("/hold")
    public ResponseEntity<Booking> createBooking(@RequestBody HoldRequest req) {
        Booking b = bookingService.createBooking(req);
        return ResponseEntity.ok(b);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id) {
        Booking b = bookingService.confirmBooking(id);
        return ResponseEntity.ok(b);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id, @RequestParam(required = false) String reason) {
        Booking b = bookingService.cancelBooking(id, reason);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        Booking b = bookingService.getBooking(id);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getBookingsByUser(user));
    }

    // Admin endpoints
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        Booking b = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(b);
    }
}