package com.busreservation.controller;

import com.busreservation.model.Route;
import com.busreservation.service.RouteService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route saved = routeService.createRoute(route);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route route) {
        // keep update logic simple here (service.save will overwrite)
        Route existing = routeService.getRouteById(id);
        existing.setSource(route.getSource());
        existing.setDestination(route.getDestination());
        existing.setDistance(route.getDistance());
        existing.setDuration(route.getDuration());
        Route saved = routeService.createRoute(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
