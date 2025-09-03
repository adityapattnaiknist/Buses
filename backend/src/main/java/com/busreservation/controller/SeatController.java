package com.busreservation.controller;

import com.busreservation.model.Seat;
import com.busreservation.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;
    public SeatController(SeatService seatService) { this.seatService = seatService; }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.create(seat));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Seat>> byTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(seatService.getByTrip(tripId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> update(@PathVariable Long id, @RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.update(id, seat));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
