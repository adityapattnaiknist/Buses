package com.busreservation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busNumber;
    private String type;
    private Integer capacity;
    private Integer availableSeats;

    @ManyToOne
    private Route route;
}
