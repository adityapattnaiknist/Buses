package com.busreservation.repository;

import com.busreservation.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByBookingId(Long bookingId);

    interface TicketReportProjection {
        Long getTicketId();
        String getTicketNumber();
        Long getBookingId();
    }

    @Query("SELECT t.id as ticketId, t.ticketNumber as ticketNumber, t.booking.id as bookingId FROM Ticket t")
    List<TicketReportProjection> findAllTicketReportData();
}
