package com.busreservation.controller;

import com.busreservation.model.Seat;
import com.busreservation.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;
    public SeatController(SeatService seatService) { this.seatService = seatService; }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.create(seat));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Seat>> byTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(seatService.getByTrip(tripId));
    }
}
