package com.busreservation.repository;

import com.busreservation.model.Route;
import com.busreservation.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByBus_Route(Route route);

    @Query("SELECT t FROM Trip t WHERE t.source = :source AND t.destination = :destination AND t.departureTime BETWEEN :startOfDay AND :endOfDay")
    List<Trip> searchTrips(@Param("source") String source, @Param("destination") String destination, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
