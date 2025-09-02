package com.busreservation.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.busreservation.service.PaymentService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam Long bookingId, @RequestParam Double amount) {
        return ResponseEntity.ok(paymentService.checkout(bookingId, amount));
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestParam Long paymentId) {
        return ResponseEntity.ok(paymentService.refund(paymentId));
    }
}
