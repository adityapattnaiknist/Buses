package com.busreservation.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketNumber;
    private String filePath;

    @OneToOne
    private Booking booking;
    private LocalDateTime createdAt;
}
