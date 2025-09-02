package com.busreservation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cancellations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancellation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Booking booking;

    private LocalDateTime cancellationTime;
    private String reason;
}
