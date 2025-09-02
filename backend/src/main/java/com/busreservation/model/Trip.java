package com.busreservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String destination;
    private double distance;
    private String duration;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double fare;

    @ManyToOne
    private Bus bus;

    @OneToMany(mappedBy = "trip")
    private List<Seat> seats;
}
