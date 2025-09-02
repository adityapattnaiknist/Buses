package com.busreservation.service.implementations;

import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.dto.HoldRequest;
import com.busreservation.model.Booking;
import com.busreservation.model.Ticket;
import com.busreservation.model.User;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.TicketRepository;
import com.busreservation.repository.TripRepository;
import com.busreservation.repository.UserRepository;
import com.busreservation.service.BookingService;
import com.busreservation.service.EmailService;
import com.busreservation.service.TicketPdfService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketPdfService ticketPdfService;
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              TripRepository tripRepository,
                              UserRepository userRepository,
                              TicketRepository ticketRepository,
                              TicketPdfService ticketPdfService,
                              EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.ticketPdfService = ticketPdfService;
        this.emailService = emailService;
    }

    @Override
    public Booking createBooking(HoldRequest request) {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        booking.setAmount(request.getAmount());
        booking.setCreatedAt(LocalDateTime.now());

        // Set user/trip if repositories exist
        if (request.getUserId() != null && userRepository != null) {
            User user = userRepository.findById(request.getUserId()).orElse(null);
            booking.setUser(user);
        }
        if (request.getTripId() != null && tripRepository != null) {
            tripRepository.findById(request.getTripId()).ifPresent(booking::setTrip);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        String filePath = null;

        try {
            // Create and save the ticket
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setTicketNumber("TICKET-" + UUID.randomUUID());
            ticket.setCreatedAt(LocalDateTime.now());

            // Generate PDF and set file path
            filePath = ticketPdfService.generateTicketPdf(booking);
            ticket.setFilePath(filePath);

            ticketRepository.save(ticket);

            // Associate ticket with booking
            booking.setTicket(ticket);

        } catch (IOException e) {
            // In a real app, you might want a more sophisticated error handling
            // like a transactional rollback.
            e.printStackTrace();
            throw new RuntimeException("Failed to generate ticket PDF.", e);
        }

        Booking savedBooking = bookingRepository.save(booking);

        // Send email confirmation
        if (filePath != null) {
            emailService.sendBookingConfirmation(savedBooking.getUser(), savedBooking, filePath);
        }

        return savedBooking;
    }

    @Override
    public Booking holdBooking(Long userId, Long tripId, double amount) {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.HOLD);
        booking.setAmount(amount);
        booking.setCreatedAt(LocalDateTime.now());
        // optional: set user/trip if repos available
        if (userRepository != null && userId != null) {
            userRepository.findById(userId).ifPresent(booking::setUser);
        }
        if (tripRepository != null && tripId != null) {
            tripRepository.findById(tripId).ifPresent(booking::setTrip);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        String filePath = null;

        try {
            filePath = ticketPdfService.generateCancellationReceiptPdf(booking);
        } catch (IOException e) {
            // Log the error, but don't prevent the cancellation from completing.
            e.printStackTrace();
        }

        Booking savedBooking = bookingRepository.save(booking);

        // Send cancellation email
        if (filePath != null) {
            emailService.sendCancellationConfirmation(savedBooking.getUser(), savedBooking, filePath);
        }

        return savedBooking;
    }

    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
    }

    @Override
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
}
