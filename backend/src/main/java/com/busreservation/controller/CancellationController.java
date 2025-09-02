package com.busreservation.controller;

import com.busreservation.dto.CancelRequest;
import com.busreservation.model.Booking;
import com.busreservation.service.CancellationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cancel")
public class CancellationController {
    private final CancellationService cancellationService;
    public CancellationController(CancellationService cancellationService) {
        this.cancellationService = cancellationService;
    }

    @PostMapping
    public ResponseEntity<Booking> cancel(@RequestBody CancelRequest req) {
        return ResponseEntity.ok(cancellationService.cancelBooking(req));
    }
}
