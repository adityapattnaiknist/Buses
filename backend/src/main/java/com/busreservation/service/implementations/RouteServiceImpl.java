package com.busreservation.service.implementations;

import com.busreservation.model.Route;
import com.busreservation.repository.RouteRepository;
import com.busreservation.service.RouteService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository repo;

    public RouteServiceImpl(RouteRepository repo) {
        this.repo = repo;
    }

    @Override
    public Route createRoute(Route route) {
        return repo.save(route);
    }

    @Override
    public List<Route> getAllRoutes() {
        return repo.findAll();
    }

    @Override
    public Route getRouteById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public void deleteRoute(Long id) {
        repo.deleteById(id);
    }
}
