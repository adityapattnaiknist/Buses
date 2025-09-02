package com.busreservation.service.implementations;

import com.busreservation.model.Booking;
import com.busreservation.model.Ticket;
import com.busreservation.model.Trip;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.RouteRepository;
import com.busreservation.repository.TicketRepository;
import com.busreservation.repository.TripRepository;
import com.busreservation.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;

    public ReportServiceImpl(BookingRepository bookingRepository, TicketRepository ticketRepository, TripRepository tripRepository, RouteRepository routeRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.tripRepository = tripRepository;
        this.routeRepository = routeRepository;
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

    @Override
    public List<Map<String, Object>> generateSalesReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        double totalSales = 0;
        for (Booking booking : bookingRepository.findAll()) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                totalSales += booking.getAmount();
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("totalSales", totalSales);
        report.add(data);
        return report;
    }

    @Override
    public List<Map<String, Object>> generateOccupancyReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Trip trip : tripRepository.findAll()) {
            Map<String, Object> data = new HashMap<>();
            data.put("tripId", trip.getId());
            data.put("busNumber", trip.getBus().getBusNumber());
            data.put("totalSeats", trip.getBus().getCapacity());
            long bookedSeats = bookingRepository.findByTrip(trip).size();
            data.put("bookedSeats", bookedSeats);
            data.put("occupancy", (double) bookedSeats / trip.getBus().getCapacity());
            report.add(data);
        }
        return report;
    }

    @Override
    public List<Map<String, Object>> generateRoutePerformanceReport() {
        // This is a placeholder implementation.
        // A more detailed implementation would require more complex queries.
        List<Map<String, Object>> report = new ArrayList<>();
        routeRepository.findAll().forEach(route -> {
            Map<String, Object> data = new HashMap<>();
            data.put("routeId", route.getId());
            data.put("source", route.getSource());
            data.put("destination", route.getDestination());
            long totalBookings = tripRepository.findByBus_Route(route).stream()
                    .mapToLong(trip -> bookingRepository.findByTrip(trip).size())
                    .sum();
            data.put("totalBookings", totalBookings);
            report.add(data);
        });
        return report;
    }
}
