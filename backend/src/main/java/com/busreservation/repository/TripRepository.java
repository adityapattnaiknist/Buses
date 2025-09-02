package com.busreservation.repository;

import com.busreservation.model.Route;
import com.busreservation.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByBus_Route(Route route);
}
