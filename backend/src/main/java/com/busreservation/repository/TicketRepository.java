package com.busreservation.repository;

import com.busreservation.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByBookingId(Long bookingId);
}
