package com.busreservation.service;

import com.busreservation.model.Seat;

import java.util.List;

public interface SeatService {
    Seat create(Seat seat);
    List<Seat> getByTrip(Long tripId);
    Seat update(Long id, Seat seat);
    void delete(Long id);
}
