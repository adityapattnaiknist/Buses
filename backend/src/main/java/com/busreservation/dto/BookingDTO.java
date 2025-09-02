package com.busreservation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {
    public Long id;
    public TripDTO trip;
    public UserDTO user;
    public Double amount;
    public String status; // HOLD | CONFIRMED | CANCELLED
    public LocalDateTime createdAt;
    public LocalDateTime confirmedAt;
    public LocalDateTime cancelledAt;
    public List<SeatDTO> seats;
}
