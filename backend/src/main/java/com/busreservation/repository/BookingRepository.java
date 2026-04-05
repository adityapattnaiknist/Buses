package com.busreservation.repository;

import com.busreservation.model.Booking;
import com.busreservation.model.Trip;
import com.busreservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrip(Trip trip);
    List<Booking> findByUser(User user);

    @Query("SELECT b.id AS id, b.status AS status, b.amount AS amount FROM Booking b")
    List<BookingReportProjection> findAllBookingReportData();
}
