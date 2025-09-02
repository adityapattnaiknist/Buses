package com.busreservation.service;

import com.busreservation.model.Trip;
import com.busreservation.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public List<Trip> searchTrips(String source, String destination, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return tripRepository.searchTrips(source, destination, startOfDay, endOfDay);
    }

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, Trip updatedTrip) {
        Trip t = getTripById(id);
        t.setSource(updatedTrip.getSource());
        t.setDestination(updatedTrip.getDestination());
        t.setDistance(updatedTrip.getDistance());
        t.setDuration(updatedTrip.getDuration());
        t.setFare(updatedTrip.getFare());
        t.setDepartureTime(updatedTrip.getDepartureTime());
        t.setArrivalTime(updatedTrip.getArrivalTime());
        t.setBus(updatedTrip.getBus());
        return tripRepository.save(t);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
