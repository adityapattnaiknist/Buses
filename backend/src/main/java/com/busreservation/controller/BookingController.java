package com.busreservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.service.BookingService;

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
}