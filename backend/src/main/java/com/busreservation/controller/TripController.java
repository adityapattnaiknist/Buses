package com.busreservation.controller;

import com.busreservation.model.Trip;
import com.busreservation.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping("/{id}")
    public Trip getTrip(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    @GetMapping
    public List<Trip> list() {
        return tripService.getAllTrips();
    }

    @PostMapping
    public Trip create(@RequestBody Trip trip) {
        return tripService.createTrip(trip);
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable Long id, @RequestBody Trip trip) {
        return tripService.updateTrip(id, trip);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tripService.deleteTrip(id);
    }
}
