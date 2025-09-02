package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.Ticket;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.TicketRepository;
import com.busreservation.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    public ReportServiceImpl(BookingRepository bookingRepository, TicketRepository ticketRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Map<String, Object>> generateBookingReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Booking booking : bookingRepository.findAll()) {
            Map<String, Object> data = new HashMap<>();
            data.put("bookingId", booking.getId());
            data.put("status", booking.getStatus());
            data.put("amount", booking.getAmount());
            report.add(data);
        }
        return report;
    }

    @Override
    public List<Map<String, Object>> generateTicketReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findAll()) {
            Map<String, Object> data = new HashMap<>();
            data.put("ticketId", ticket.getId());
            data.put("ticketNumber", ticket.getTicketNumber());
            data.put("bookingId", ticket.getBooking().getId());
            report.add(data);
        }
        return report;
    }
}
