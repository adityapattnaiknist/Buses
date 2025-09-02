package com.busreservation.repository;

import com.busreservation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // add custom queries if needed
}
