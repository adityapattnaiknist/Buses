package com.busreservation.controller;

import com.busreservation.model.Booking;
import com.busreservation.service.BookingService;
import com.busreservation.service.implementations.TicketPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final BookingService bookingService;
    private final TicketPdfService ticketPdfService;

    public TicketController(BookingService bookingService, TicketPdfService ticketPdfService) {
        this.bookingService = bookingService;
        this.ticketPdfService = ticketPdfService;
    }

    /**
     * Download ticket PDF for a booking (attachment).
     * Example: GET /api/tickets/1/download
     */
    @GetMapping("/{bookingId}/download")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingService.getBooking(bookingId);
            byte[] pdf = ticketPdfService.generateTicketPdfBytes(booking);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket-" + bookingId + ".pdf");
            headers.setContentLength(pdf.length);

            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = "Unable to generate ticket: " + ex.getMessage();
            return ResponseEntity.internalServerError()
                    .header("Content-Type", "text/plain")
                    .body(msg.getBytes());
        }
    }
}
