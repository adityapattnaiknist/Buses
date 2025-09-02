package com.busreservation.util;

import com.busreservation.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps JSON stable (always include bus & seats) and uses LocalDateTime
 * consistently — no OffsetDateTime conversions here anymore.
 */
public class EntityDtoMapper {

    public static Seat toSeatDto(Seat e) {
        Seat dto = new Seat();
        dto.setId(e.getId());
        dto.setSeatNumber(e.getSeatNumber());
        dto.setAvailable(e.isAvailable());
        if (e.getTrip() != null) {
            Trip t = new Trip();
            t.setId(e.getTrip().getId());
            dto.setTrip(t);
        }
        if (e.getBooking() != null) {
            Booking b = new Booking();
            b.setId(e.getBooking().getId());
            dto.setBooking(b);
        }
        return dto;
    }

    public static Trip toTripDto(Trip e) {
        Trip dto = new Trip();
        dto.setId(e.getId());
        dto.setSource(e.getSource());
        dto.setDestination(e.getDestination());
        dto.setDistance(e.getDistance());
        dto.setDuration(e.getDuration());
        dto.setDepartureTime(e.getDepartureTime());
        dto.setArrivalTime(e.getArrivalTime());
        dto.setFare(e.getFare());

        if (e.getBus() != null) {
            Bus b = new Bus();
            b.setId(e.getBus().getId());
            b.setBusNumber(e.getBus().getBusNumber());
            b.setType(e.getBus().getType());
            b.setCapacity(e.getBus().getCapacity());
            b.setAvailableSeats(e.getBus().getAvailableSeats());
            if (e.getBus().getRoute() != null) {
                Route r = new Route();
                r.setId(e.getBus().getRoute().getId());
                r.setSource(e.getBus().getRoute().getSource());
                r.setDestination(e.getBus().getRoute().getDestination());
                r.setDistance(e.getBus().getRoute().getDistance());
                r.setDuration(e.getBus().getRoute().getDuration());
                b.setRoute(r);
            }
            dto.setBus(b);
        }

        if (e.getSeats() != null) {
            List<Seat> seats = e.getSeats().stream()
                    .map(EntityDtoMapper::toSeatDto)
                    .collect(Collectors.toList());
            dto.setSeats(seats);
        }
        return dto;
    }

    public static Booking toBookingDto(Booking e) {
        Booking dto = new Booking();
        dto.setId(e.getId());
        dto.setTrip(e.getTrip() != null ? toTripDto(e.getTrip()) : null);
        if (e.getUser() != null) {
            User u = new User();
            u.setId(e.getUser().getId());
            u.setUsername(e.getUser().getUsername());
            u.setName(e.getUser().getName());
            u.setEmail(e.getUser().getEmail());
            u.setRole(e.getUser().getRole());
            dto.setUser(u);
        }
        dto.setAmount(e.getAmount());
        dto.setStatus(e.getStatus());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setConfirmedAt(e.getConfirmedAt());
        dto.setCancelledAt(e.getCancelledAt());
        if (e.getSeats() != null) {
            dto.setSeats(e.getSeats().stream().map(EntityDtoMapper::toSeatDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static Ticket toTicketDto(Ticket e) {
        Ticket dto = new Ticket();
        dto.setId(e.getId());
        dto.setTicketNumber(e.getTicketNumber());
        dto.setFilePath(e.getFilePath());
        dto.setBooking(e.getBooking() != null ? toBookingDto(e.getBooking()) : null);
        return dto;
    }
}
