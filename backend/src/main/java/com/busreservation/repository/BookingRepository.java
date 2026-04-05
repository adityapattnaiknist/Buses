package com.busreservation.repository;

import com.busreservation.model.Booking;
import com.busreservation.model.Trip;
import com.busreservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrip(Trip trip);
    List<Booking> findByUser(User user);

    @Query("SELECT new map(b.id as bookingId, b.status as status, b.amount as amount) FROM Booking b")
    List<Map<String, Object>> findAllBookingReportData();
}
