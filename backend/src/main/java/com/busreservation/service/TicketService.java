package com.busreservation.service;

import java.io.IOException;
import java.util.Optional;

import com.busreservation.model.Ticket;

public interface TicketService {
    Optional<Ticket> generateTicket(Long bookingId) throws IOException;
    Optional<Ticket> getTicket(Long id);
    byte[] downloadTicketPdf(Long ticketId) throws IOException;
}