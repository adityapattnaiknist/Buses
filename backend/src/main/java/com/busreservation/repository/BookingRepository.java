package com.busreservation.repository;

import com.busreservation.model.Booking;
import com.busreservation.model.Trip;
import com.busreservation.model.User;
import com.busreservation.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrip(Trip trip);
    List<Booking> findByUser(User user);

    @Query("SELECT COALESCE(SUM(b.amount), 0.0) FROM Booking b WHERE b.status = :status")
    Double sumAmountByStatus(@Param("status") BookingStatus status);
}
