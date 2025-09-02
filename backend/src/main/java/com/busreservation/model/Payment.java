package com.busreservation.model;

import com.busreservation.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    private Booking booking;
}
