package com.busreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripResponse {
    private Long id;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private String busNumber;
    private String busType;
    private int capacity;
    private int availableSeats;
}
