package com.busreservation.service;

import com.busreservation.model.Route;
import java.util.List;

public interface RouteService {
    Route createRoute(Route route);
    List<Route> getAllRoutes();
    Route getRouteById(Long id);
    void deleteRoute(Long id);
}
