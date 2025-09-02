package com.busreservation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer seatNumber;
    private boolean available;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private Booking booking;
}
