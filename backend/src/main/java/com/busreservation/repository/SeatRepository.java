package com.busreservation.repository;

import com.busreservation.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Needed by SeatServiceImpl (your error)
    List<Seat> findByTripId(Long tripId);

    // Used for allocating seats during hold/createBooking
    List<Seat> findByTripIdAndAvailableTrueOrderBySeatNumber(Long tripId);
}
