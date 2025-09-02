package com.busreservation.service;

public interface PaymentService {
    String checkout(Long bookingId, Double amount);
    String refund(Long paymentId);
}
