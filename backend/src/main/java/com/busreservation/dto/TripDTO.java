package com.busreservation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TripDTO {
    public Long id;
    public String source;
    public String destination;
    public Double distance;
    public String duration;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;
    public Double fare;
    public BusDTO bus;
    public List<SeatDTO> seats;
}
